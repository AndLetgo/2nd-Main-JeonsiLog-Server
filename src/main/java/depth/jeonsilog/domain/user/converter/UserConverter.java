package depth.jeonsilog.domain.user.converter;

import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.dto.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    // USER -> UserResDTO
    public static UserResponseDto.UserRes toUserRes(User user) {
        return UserResponseDto.UserRes.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImg())
                .build();
    }

    // Users -> SearchUsersDTO
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
}
