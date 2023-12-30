package depth.jeonsilog.domain.user.application;

import depth.jeonsilog.domain.auth.domain.Token;
import depth.jeonsilog.domain.auth.domain.repository.TokenRepository;
import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.s3.application.S3Uploader;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final TokenRepository tokenRepository;
    private final S3Uploader s3Uploader;

    // 토큰으로 본인 정보 조회
    public ResponseEntity<?> findUserByToken(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(userRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 id로 정보 조회
    public ResponseEntity<?> findUserById(UserPrincipal userPrincipal, Long userId) {

        validateUserByToken(userPrincipal);

        User findUser = validateUserById(userId);
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(userRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 닉네임 변경
    @Transactional
    public ResponseEntity<?> changeNickname(UserPrincipal userPrincipal, UserRequestDto.ChangeNicknameReq changeNicknameReq) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateNickname(changeNicknameReq.getNickname());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("닉네임을 변경했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 프로필 변경
    @Transactional
    public ResponseEntity<?> changeProfile(UserPrincipal userPrincipal, MultipartFile img) throws IOException {
        User findUser = validateUserByToken(userPrincipal);

        if (!img.isEmpty()) {
            String storedFileName = s3Uploader.upload(img, "profile_img");
            findUser.updateProfileImg(storedFileName);
        }

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("프로필 사진을 변경했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 검색
    public ResponseEntity<?> searchUsers(UserPrincipal userPrincipal, String searchWord) {

        validateUserByToken(userPrincipal);
        List<User> findUsers = userRepository.findAllByNicknameContaining(searchWord);
        List<UserResponseDto.SearchUsersRes> searchUsersResList = UserConverter.toSearchUsersRes(findUsers);

        ApiResponse apiResponse = ApiResponse.toApiResponse(searchUsersResList);

        return ResponseEntity.ok(apiResponse);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<?> deleteUser(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);
        findUser.updateStatus(Status.DELETE);  // soft delete

        Optional<Token> token = tokenRepository.findByUserEmail(userPrincipal.getEmail());
        DefaultAssert.isTrue(token.isPresent(), "이미 탈퇴되었습니다.");
        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("회원이 탈퇴되었습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 포토 캘린더 공개/비공개 변경
    @Transactional
    public ResponseEntity<?> switchIsOpen(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsOpen(!findUser.isOpen());

        UserResponseDto.SwitchIsOpenRes switchIsOpenRes = UserConverter.toSwitchIsOpenRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsOpenRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 팔로잉 알림 수신 여부 변경
    @Transactional
    public ResponseEntity<?> switchIsRecvFollowing(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsRecvFollowing(!findUser.isRecvFollowing());

        UserResponseDto.SwitchIsRecvFollowingRes switchIsRecvFollowingRes = UserConverter.toSwitchIsRecvFollowingRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsRecvFollowingRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 나의 활동 알림 수신 여부 변경
    @Transactional
    public ResponseEntity<?> switchIsRecvActive(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsRecvActive(!findUser.isRecvActive());

        UserResponseDto.SwitchIsRecvActiveRes switchIsRecvActiveRes = UserConverter.toSwitchIsRecvActive(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsRecvActiveRes);

        return ResponseEntity.ok(apiResponse);
    }


    public User validateUserByToken(UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

    public User validateUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }
}
