package depth.jeonsilog.domain.follow.converter;

import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.follow.dto.FollowResponseDto;
import depth.jeonsilog.domain.user.domain.User;


import java.util.ArrayList;
import java.util.List;

public class FollowConverter {

    // -> Follow
    public static Follow toFollow(User user, User followUser) {

        return Follow.builder()
                .user(user)
                .follow(followUser)
                .build();
    }

    // Users -> MyFollowingListRes
    public static List<FollowResponseDto.MyFollowingListRes> toMyFollowingListRes(List<Follow> follows) {

        List<FollowResponseDto.MyFollowingListRes> followingListRes = new ArrayList<>();

        for (Follow follow : follows) {
            FollowResponseDto.MyFollowingListRes followRes = FollowResponseDto.MyFollowingListRes.builder()
                    .followUserId(follow.getFollow().getId())
                    .nickname(follow.getFollow().getNickname())
                    .profileImgUrl(follow.getFollow().getProfileImg())
                    .build();

            followingListRes.add(followRes);
        }
        return followingListRes;
    }

    // Users -> MyFollowerListRes
    public static List<FollowResponseDto.MyFollowerListRes> toMyFollowerListRes(List<Follow> follows, FollowRepository followRepository) {

        List<FollowResponseDto.MyFollowerListRes> followerListRes = new ArrayList<>();

        for (Follow follow : follows) {
            FollowResponseDto.MyFollowerListRes followRes = FollowResponseDto.MyFollowerListRes.builder()
                    .followUserId(follow.getUser().getId())
                    .nickname(follow.getUser().getNickname())
                    .profileImgUrl(follow.getUser().getProfileImg())
                    .isIFollow(followRepository.findByUserAndFollow(follow.getFollow(), follow.getUser()).isPresent())
                    .build();

            followerListRes.add(followRes);
        }
        return followerListRes;
    }

    // Users -> toUserFollowingListRes
    public static List<FollowResponseDto.UserFollowingListRes> toUserFollowingListRes(User me, List<Follow> follows, FollowRepository followRepository) {

        List<FollowResponseDto.UserFollowingListRes> followerListRes = new ArrayList<>();

        for (Follow follow : follows) {
            FollowResponseDto.UserFollowingListRes followRes = FollowResponseDto.UserFollowingListRes.builder()
                    .followUserId(follow.getFollow().getId())
                    .nickname(follow.getFollow().getNickname())
                    .profileImgUrl(follow.getFollow().getProfileImg())
                    .isIFollow(followRepository.findByUserAndFollow(me, follow.getFollow()).isPresent())
                    .isFollowMe(followRepository.findByUserAndFollow(follow.getFollow(), me).isPresent())
                    .build();

            followerListRes.add(followRes);
        }
        return followerListRes;
    }

    // Users -> toUserFollowerListRes
    public static List<FollowResponseDto.UserFollowerListRes> toUserFollowerListRes(User me, List<Follow> follows, FollowRepository followRepository) {

        List<FollowResponseDto.UserFollowerListRes> followerListRes = new ArrayList<>();

        for (Follow follow : follows) {
            FollowResponseDto.UserFollowerListRes followRes = FollowResponseDto.UserFollowerListRes.builder()
                    .followUserId(follow.getUser().getId())
                    .nickname(follow.getUser().getNickname())
                    .profileImgUrl(follow.getUser().getProfileImg())
                    .isIFollow(followRepository.findByUserAndFollow(me, follow.getUser()).isPresent())
                    .isFollowMe(followRepository.findByUserAndFollow(follow.getUser(), me).isPresent())
                    .build();

            followerListRes.add(followRes);
        }
        return followerListRes;
    }
}
