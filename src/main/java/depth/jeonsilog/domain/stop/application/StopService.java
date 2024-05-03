package depth.jeonsilog.domain.stop.application;

import depth.jeonsilog.domain.stop.domain.Stop;
import depth.jeonsilog.domain.stop.domain.repository.StopRepository;
import depth.jeonsilog.domain.stop.dto.StopRequestDto;
import depth.jeonsilog.domain.stop.dto.StopResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StopService {

    private final StopRepository stopRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskScheduler taskScheduler;

    // TODO: 유저 정지
    @Transactional
    public ResponseEntity<?> stopUser(final UserPrincipal userPrincipal, final StopRequestDto.StopUserReq dto) {
        User admin = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(admin.getRole().equals(Role.ADMIN), "관리자만 유저를 정지할 수 있습니다.");

        Optional<User> targetUser = userRepository.findById(dto.getUserId());
        DefaultAssert.isTrue(targetUser.isPresent(), "정지시키려는 유저가 올바르지 않습니다.");

        Stop stop = Stop.builder()
                .user(targetUser.get())
                .reason(dto.getReason())
                .build();

        Stop entity = stopRepository.save(stop);
        scheduleDeletion(entity.getId(), entity.getCreatedDate());

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("유저를 정지했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    private void scheduleDeletion(Long entityId, LocalDateTime createdAt) {
        LocalDateTime deletionTime = createdAt.plusDays(1);
        taskScheduler.schedule(() -> deleteStopEntity(entityId), Date.from(deletionTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private void deleteStopEntity(final Long stopId) {
        stopRepository.deleteById(stopId);
    }

    // TODO: 유저 정지 조회
    public ResponseEntity<?> findUserIsStop(final UserPrincipal userPrincipal) {
        User user = userService.validateUserByToken(userPrincipal);
        Optional<Stop> stop = stopRepository.findByUserId(user.getId());
        DefaultAssert.isTrue(stop.isPresent(), "유저 정지 이력이 없습니다.");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = stop.get().getCreatedDate().plusDays(7);
        Integer between = target.getDayOfMonth() - now.getDayOfMonth();

        StopResponseDto.StopUserRes dto = StopResponseDto.StopUserRes.builder()
                .reason(stop.get().getReason())
                .remainingDays(between)
                .build();

        ApiResponse apiResponse = ApiResponse.toApiResponse(dto);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 정지된 유저 앱 최초접속 확인
    @Transactional
    public ResponseEntity<?> updateIsFirstAccess(final UserPrincipal userPrincipal) {
        User user = userService.validateUserByToken(userPrincipal);
        Optional<Stop> stop = stopRepository.findByUserId(user.getId());
        DefaultAssert.isTrue(stop.isPresent(), "유저 정지 이력이 없습니다.");

        stop.get().updateIsFirstAccess();

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("정지 후 최초 접속 확인.").build());
        return ResponseEntity.ok(apiResponse);
    }
}
