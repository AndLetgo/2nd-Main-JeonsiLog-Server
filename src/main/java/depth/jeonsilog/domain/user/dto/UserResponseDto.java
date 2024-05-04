package depth.jeonsilog.domain.user.dto;


import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class UserResponseDto {

    @Data
    @Builder
    public static class UserRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "해당 유저가 관리자인지 출력합니다. ADMIN : isAdmin = true")
        private Boolean isAdmin;

        @Schema(type = "string", example = "루피", description = "닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description = "프로필 이미지 url을 출력합니다.")
        private String profileImgUrl;

        @Schema(type = "int", example = "13", description = "유저의 팔로잉 숫자를 출력합니다.")
        private Integer numFollowing;

        @Schema(type = "int", example = "20", description = "유저의 팔로워 숫자를 출력합니다.")
        private Integer numFollower;

        @Schema(type = "int", example = "5", description = "유저의 감상평 개수를 출력합니다.")
        private Integer reviewCount;

        @Schema(type = "UserLevel", example = "BEGINNER", description = "유저의 레벨을 출력합니다. NON(0), DONE(1 ~ 2), BEGINNER(3 ~ 9), INTERMEDIATE(10 ~ 19), ADVANCED(20 ~ 29), MASTER(30 ~)")
        private UserLevel userLevel;
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

        @Schema(type = "UserLevel", example = "BEGINNER", description = "유저의 레벨을 출력합니다. NON(0), DONE(1 ~ 2), BEGINNER(3 ~ 9), INTERMEDIATE(10 ~ 19), ADVANCED(20 ~ 29), MASTER(30 ~)")
        private UserLevel userLevel;
    }

    @Data
    @Builder
    public static class SearchUserWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<UserResponseDto.SearchUsersRes> data;
    }

    @Data
    @Builder
    public static class SwitchIsOpenRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 포토캘린더 공개/비공개 여부를 출력합니다.")
        private Boolean isOpen;
    }

    @Data
    @Builder
    public static class SwitchIsRecvExhibitionRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 전시 알림 수신 여부를 출력합니다.")
        private Boolean isRecvExhbition;
    }

    @Data
    @Builder
    public static class SwitchIsRecvActiveRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 활동 알림 수신 여부를 출력합니다.")
        private Boolean isRecvActive;
    }

    @Data
    @Builder
    public static class IsOpenRes {

        @Schema(type = "boolean", example = "true", description = "유저의 포토캘린더 공개/비공개 여부를 출력합니다.")
        private Boolean isOpen;
    }

    @Data
    @Builder
    public static class IsRecvOrNotRes {

        @Schema(type = "long", example = "1", description = "유저의 ID를 출력합니다.")
        private Long userId;

        @Schema(type = "boolean", example = "true", description = "유저의 전시 알림 수신 여부를 출력합니다.")
        private Boolean isRecvExhibition;

        @Schema(type = "boolean", example = "true", description = "유저의 활동 알림 수신 여부를 출력합니다.")
        private Boolean isRecvActive;
    }
}
