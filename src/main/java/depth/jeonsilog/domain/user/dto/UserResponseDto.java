package depth.jeonsilog.domain.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class UserResponseDto {

    @Data
    @Builder
    public static class UserRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;

        // 팔로우 정보는 추후 추가 예정 //
    }

    @Data
    @Builder
    public static class SearchUsersRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;
    }

    @Data
    @Builder
    public static class SwitchIsOpenRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 포토캘린더 공개/비공개 여부를 출력합니다.")
        private boolean isOpen;
    }

    @Data
    @Builder
    public static class SwitchIsRecvFollowingRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 팔로잉 알림 수신 여부를 출력합니다.")
        private boolean isRecvFollowing;
    }

    @Data
    @Builder
    public static class SwitchIsRecvActiveRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 나의 활동 알림 수신 여부를 출력합니다.")
        private boolean isRecvActive;
    }
}