package depth.jeonsilog.domain.alarm.application;

import depth.jeonsilog.domain.alarm.converter.AlarmConverter;
import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.alarm.domain.repository.AlarmRepository;
import depth.jeonsilog.domain.alarm.dto.AlarmResponseDto;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;

    // TODO: 활동 알림 목록 조회
    public ResponseEntity<?> getActivityAlarmList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<AlarmType> types = Arrays.asList(AlarmType.RATING, AlarmType.REVIEW, AlarmType.REPLY, AlarmType.FOLLOW);

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Alarm> alarmPage = alarmRepository.findSliceByUserIdAndAlarmTypeIn(pageRequest, findUser.getId(), types);
        List<Alarm> alarmList = alarmPage.getContent();

        DefaultAssert.isTrue(!alarmList.isEmpty(), "활동 알림이 존재하지 않습니다.");

        List<AlarmResponseDto.AlarmRes> alarmResList = new ArrayList<>();
        for (Alarm alarm : alarmList) {
            Optional<User> sender = userRepository.findById(alarm.getSenderId());
            DefaultAssert.isTrue(sender.isPresent());

            String content = "";
            switch (alarm.getAlarmType()) {
                case REVIEW -> content = sender.get().getNickname() + " 님이 감상평을 남겼어요";
                case RATING -> content = sender.get().getNickname() + " 님이 별점을 남겼어요";
                case REPLY -> content = sender.get().getNickname() + " 님이 내 감상평에 댓글을 남겼어요";
                case FOLLOW -> content = sender.get().getNickname() + " 님이 나를 팔로우해요";
            }

            AlarmResponseDto.AlarmRes alarmRes = AlarmResponseDto.AlarmRes.builder()
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .targetId(alarm.getTargetId())
                    .clickId(alarm.getClickId())
                    .imgUrl(sender.get().getProfileImg())
                    .contents(content)
                    .dateTime(alarm.getCreatedDate())
                    .isChecked(alarm.getIsChecked())
                    .build();
            alarmResList.add(alarmRes);
        }

        boolean hasNextPage = alarmPage.hasNext();
        AlarmResponseDto.AlarmResListWithPaging alarmResListWithPaging = AlarmConverter.toAlarmResListWithPaging(hasNextPage, alarmResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(alarmResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 전시 알림 목록 조회
    public ResponseEntity<?> getExhibitionAlarmList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<AlarmType> types = List.of(AlarmType.EXHIBITION);

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Alarm> alarmPage = alarmRepository.findSliceByUserIdAndAlarmTypeIn(pageRequest, findUser.getId(), types);
        List<Alarm> alarmList = alarmPage.getContent();

        DefaultAssert.isTrue(!alarmList.isEmpty(), "전시 알림이 존재하지 않습니다.");

        List<AlarmResponseDto.AlarmRes> alarmResList = new ArrayList<>();
        for (Alarm alarm : alarmList) {
            Optional<Exhibition> exhibition = exhibitionRepository.findById(alarm.getTargetId());
            DefaultAssert.isTrue(exhibition.isPresent());
            AlarmResponseDto.AlarmRes alarmRes = AlarmResponseDto.AlarmRes.builder()
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .targetId(alarm.getTargetId())
                    .clickId(alarm.getClickId())
                    .imgUrl(exhibition.get().getImageUrl())
                    .contents("[" + exhibition.get().getName() + "] " + alarm.getContents())
                    .dateTime(alarm.getCreatedDate())
                    .isChecked(alarm.getIsChecked())
                    .build();
            alarmResList.add(alarmRes);
        }

        boolean hasNextPage = alarmPage.hasNext();
        AlarmResponseDto.AlarmResListWithPaging alarmResListWithPaging = AlarmConverter.toAlarmResListWithPaging(hasNextPage, alarmResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(alarmResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 알림 확인
    @Transactional
    public ResponseEntity<?> checkAlarm(UserPrincipal userPrincipal, Long alarmId) {

        User findUser = userService.validateUserByToken(userPrincipal);

        Optional<Alarm> alarm = alarmRepository.findById(alarmId);
        DefaultAssert.isTrue(alarm.isPresent(), "알림 정보가 올바르지 않습니다.");
        DefaultAssert.isTrue(findUser.equals(alarm.get().getUser()), "알림 수신자만 알림을 확인할 수 있습니다.");

        Alarm findAlarm = alarm.get();
        findAlarm.updateIsChecked(true);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("알림이 확인되었습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }
}
