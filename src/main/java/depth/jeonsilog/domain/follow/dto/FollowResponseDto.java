package depth.jeonsilog.domain.follow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class FollowResponseDto {

    @Data
    @Builder
    public static class MyFollowingListRes {

        @Schema(type = "long", example = "1", description = "팔로우 유저의 ID를 출력합니다.")
        private Long followUserId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;
    }

    @Data
    @Builder
    public static class MyFollowerListRes {

        @Schema(type = "long", example = "1", description = "팔로우 유저의 ID를 출력합니다.")
        private Long followUserId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;

        @Schema(type = "boolean", example = "true", description = "내가 이 유저를 팔로우하고 있는지 출력합니다.")
        private boolean isIFollow;
    }

    @Data
    @Builder
    public static class UserFollowingListRes {

        @Schema(type = "long", example = "1", description = "팔로우 유저의 ID를 출력합니다.")
        private Long followUserId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;

        @Schema(type = "boolean", example = "true", description = "내가 이 유저를 팔로우하고 있는지 출력합니다.")
        private boolean isIFollow;

        @Schema(type = "boolean", example = "true", description = "이 유저가 나를 팔로우하고 있는지 출력합니다.")
        private boolean isFollowMe;
    }

    @Data
    @Builder
    public static class UserFollowerListRes {

        @Schema(type = "long", example = "1", description = "팔로우 유저의 ID를 출력합니다.")
        private Long followUserId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;

        @Schema(type = "boolean", example = "true", description = "내가 이 유저를 팔로우하고 있는지 출력합니다.")
        private boolean isIFollow;

        @Schema(type = "boolean", example = "true", description = "이 유저가 나를 팔로우하고 있는지 출력합니다.")
        private boolean isFollowMe;
    }
}
