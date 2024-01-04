package depth.jeonsilog.domain.follow.application;

import depth.jeonsilog.domain.alarm.application.AlarmService;
import depth.jeonsilog.domain.follow.converter.FollowConverter;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.follow.dto.FollowResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final AlarmService alarmService;

    // 팔로우하기
    @Transactional
    public ResponseEntity<?> follow(UserPrincipal userPrincipal, Long userId) {

        User findUser = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(findUser, followUser)), "나를 팔로우할 수 없습니다.");

        Follow follow = FollowConverter.toFollow(findUser, followUser);
        followRepository.save(follow);

        alarmService.makeFollowAlarm(follow);

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("[" + followUser.getNickname() + "]님을 팔로우했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 팔로우 취소하기
    @Transactional
    public ResponseEntity<?> deleteFollowing(UserPrincipal userPrincipal, Long userId) {

        User findUser = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(findUser, followUser)), "나를 팔로우 취소할 수 없습니다.");

        Optional<Follow> findFollow = followRepository.findByUserAndFollow(findUser, followUser);
        DefaultAssert.isTrue(findFollow.isPresent(), "유저를 팔로우하고있지 않습니다.");

        followRepository.delete(findFollow.get());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("[" + followUser.getNickname() + "]님을 팔로우 취소했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 팔로워 삭제하기
    @Transactional
    public ResponseEntity<?> deleteFollower(UserPrincipal userPrincipal, Long userId) {

        User findUser = userService.validateUserByToken(userPrincipal);
        User followUser = userService.validateUserById(userId);
        DefaultAssert.isTrue((!Objects.equals(findUser, followUser)), "나를 팔로워 취소할 수 없습니다.");

        Optional<Follow> findFollow = followRepository.findByUserAndFollow(followUser, findUser);
        DefaultAssert.isTrue(findFollow.isPresent(), "해당 유저가 나를 팔로우하지 않습니다.");

        followRepository.delete(findFollow.get());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("[" + followUser.getNickname() + "]님을 팔로워에서 삭제했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 나의 팔로잉 리스트 조회
    public ResponseEntity<?> findMyFollowingList(UserPrincipal userPrincipal) {

        User findUser = userService.validateUserByToken(userPrincipal);
        List<Follow> follows = followRepository.findAllByUser(findUser);

        List<FollowResponseDto.MyFollowingListRes> followListRes = FollowConverter.toMyFollowingListRes(follows);

        ApiResponse apiResponse = ApiResponse.toApiResponse(followListRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 나의 팔로워 리스트 조회
    public ResponseEntity<?> findMyFollowerList(UserPrincipal userPrincipal) {

        User findUser = userService.validateUserByToken(userPrincipal);
        List<Follow> follows = followRepository.findAllByFollow(findUser);

        List<FollowResponseDto.MyFollowerListRes> followingListRes = FollowConverter.toMyFollowerListRes(follows, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(followingListRes);

        return ResponseEntity.ok(apiResponse);
    }



    // 유저의 팔로잉 리스트 조회
    public ResponseEntity<?> findUserFollowingList(UserPrincipal userPrincipal, Long userId) {

        User me = userService.validateUserByToken(userPrincipal);
        User findUser = userService.validateUserById(userId);
        List<Follow> follows = followRepository.findAllByUser(findUser);

        List<FollowResponseDto.UserFollowingListRes> followListRes = FollowConverter.toUserFollowingListRes(me, follows, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(followListRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 유저의 팔로워 리스트 조회
    public ResponseEntity<?> findUserFollowerList(UserPrincipal userPrincipal, Long userId) {

        User me = userService.validateUserByToken(userPrincipal);
        User findUser = userService.validateUserById(userId);
        List<Follow> follows = followRepository.findAllByFollow(findUser);

        List<FollowResponseDto.UserFollowerListRes> followingListRes = FollowConverter.toUserFollowerListRes(me, follows, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(followingListRes);

        return ResponseEntity.ok(apiResponse);
    }
}
