package depth.jeonsilog.domain.user.application;

import depth.jeonsilog.domain.user.converter.UserConverter;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.domain.user.dto.UserRequestDto;
import depth.jeonsilog.domain.user.dto.UserResponseDto;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    // 토큰으로 본인 정보 조회
    public ResponseEntity<?> findUserByToken(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);
        System.out.println(findUser.getEmail());
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 id로 정보 조회
    public ResponseEntity<?> findUserById(UserPrincipal userPrincipal, Long userId) {

        validateUserByToken(userPrincipal);

        User findUser = validateUserById(userId);
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 닉네임 변경
    @Transactional
    public ResponseEntity<?> changeNickname(UserPrincipal userPrincipal, UserRequestDto.ChangeNicknameReq changeNicknameReq) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateNickname(changeNicknameReq.getNickname());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("닉네임을 변경했습니다.").build()).build();
        return ResponseEntity.ok(apiResponse);
    }

    // 유저 검색
    public ResponseEntity<?> searchUsers(UserPrincipal userPrincipal, String searchWord) {

        validateUserByToken(userPrincipal);
        List<User> findUsers = userRepository.findAllByNicknameContaining(searchWord);
        List<UserResponseDto.SearchUsersRes> searchUsersResList = UserConverter.toSearchUsersRes(findUsers);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(searchUsersResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    public User validateUserByToken(UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findByEmail(userPrincipal.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

    public User validateUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }
}
