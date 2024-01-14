package depth.jeonsilog.domain.user.converter;

import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.dto.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    // USER -> UserResDTO
    public static UserResponseDto.UserRes toUserRes(User user, FollowRepository followRepository) {
        Boolean isAdmin = false;
        if (user.getRole().equals(Role.ADMIN))
            isAdmin = true;

        return UserResponseDto.UserRes.builder()
                .userId(user.getId())
                .isAdmin(isAdmin)
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

    public static UserResponseDto.SearchUserWithPaging toSearchUserWithPaging(boolean hasNextPage, List<UserResponseDto.SearchUsersRes> SearchUsersResList) {
        return UserResponseDto.SearchUserWithPaging.builder()
                .hasNextPage(hasNextPage)
                .data(SearchUsersResList)
                .build();
    }

    // USER -> SwitchIsOpenRes
    public static UserResponseDto.SwitchIsOpenRes toSwitchIsOpenRes(User user) {
        return UserResponseDto.SwitchIsOpenRes.builder()
                .userId(user.getId())
                .isOpen(user.getIsOpen())
                .build();
    }

    // USER -> SwitchIsRecvFollowingRes
    public static UserResponseDto.SwitchIsRecvExhibitionRes toSwitchIsRecvExhibitionRes(User user) {
        return UserResponseDto.SwitchIsRecvExhibitionRes.builder()
                .userId(user.getId())
                .isRecvExhbition(user.getIsRecvExhibition())
                .build();
    }

    // USER -> SwitchIsRecvActiveRes
    public static UserResponseDto.SwitchIsRecvActiveRes toSwitchIsRecvActive(User user) {
        return UserResponseDto.SwitchIsRecvActiveRes.builder()
                .userId(user.getId())
                .isRecvActive(user.getIsRecvActive())
                .build();
    }

    public static UserResponseDto.IsOpenRes toIsOpenRes(User user) {
        return UserResponseDto.IsOpenRes.builder()
                .isOpen(user.getIsOpen())
                .build();
    }

    public static UserResponseDto.IsRecvOrNotRes toIsRecvOrNotRes(User user) {
        return UserResponseDto.IsRecvOrNotRes.builder()
                .userId(user.getId())
                .isRecvExhibition(user.getIsRecvExhibition())
                .isRecvActive(user.getIsRecvActive())
                .build();
    }

    // Review에서 사용
    public static UserResponseDto.SearchUsersRes toSearchUserRes (User user) {
        return UserResponseDto.SearchUsersRes.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImg())
                .build();
    }
}
