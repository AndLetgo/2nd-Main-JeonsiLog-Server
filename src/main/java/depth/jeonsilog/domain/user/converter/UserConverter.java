package depth.jeonsilog.domain.user.converter;

import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.dto.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    // USER -> UserResDTO
    public static UserResponseDto.UserRes toUserRes(User user, FollowRepository followRepository) {
        return UserResponseDto.UserRes.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImg())
                .numFollowing(followRepository.countByUser(user))
                .numFollower(followRepository.countByFollow(user))
                .build();
    }

    // Users -> SearchUsersRes
    public static List<UserResponseDto.SearchUsersRes> toSearchUsersRes(List<User> users) {

        List<UserResponseDto.SearchUsersRes> usersResList = new ArrayList<>();
        for (User user : users) {

            UserResponseDto.SearchUsersRes usersRes = UserResponseDto.SearchUsersRes.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .profileImgUrl(user.getProfileImg())
                    .build();

            usersResList.add(usersRes);
        }
        return usersResList;
    }

    // USER -> SwitchIsOpenRes
    public static UserResponseDto.SwitchIsOpenRes toSwitchIsOpenRes(User user) {
        return UserResponseDto.SwitchIsOpenRes.builder()
                .userId(user.getId())
                .isOpen(user.isOpen())
                .build();
    }

    // USER -> SwitchIsRecvFollowingRes
    public static UserResponseDto.SwitchIsRecvFollowingRes toSwitchIsRecvFollowingRes(User user) {
        return UserResponseDto.SwitchIsRecvFollowingRes.builder()
                .userId(user.getId())
                .isRecvFollowing(user.isRecvFollowing())
                .build();
    }

    // USER -> SwitchIsRecvActiveRes
    public static UserResponseDto.SwitchIsRecvActiveRes toSwitchIsRecvActive(User user) {
        return UserResponseDto.SwitchIsRecvActiveRes.builder()
                .userId(user.getId())
                .isRecvActive(user.isRecvActive())
                .build();
    }
}
