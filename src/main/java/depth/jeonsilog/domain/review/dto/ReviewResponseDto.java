package depth.jeonsilog.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ReviewResponseDto {

    @Data
    @Builder
    public static class ReviewListRes {

        @Schema(type = "long", example = "1", description = "감상평 id를 출력합니다.")
        private Long reviewId;

        @Schema(type = "long", example = "1", description = "유저 id를 출력합니다.")
        private Long userId;

        @Schema(type = "string", example = "http://..", description = "유저의 프로필 이미지 url을 출력합니다.")
        private String imgUrl;

        @Schema(type = "string", example = "루피", description = "유저의 닉네임을 출력합니다.")
        private String nickname;

        @Schema(type = "string", example = "멋진 전시였다!", description = "감상평을 출력합니다.")
        private String contents;

        @Schema(type = "double", example = "4.5", description = "유저가 남긴 별점을 출력합니다.")
        private Double rate;

        @Schema(type = "integer", example = "8", description = "감상평에 달린 댓글의 개수를 출력합니다.")
        private Integer numReply;
    }

    @Data
    @Builder
    public static class UserReviewRes {

        @Schema(type = "long", example = "1", description = "감상평 id를 출력합니다.")
        private Long reviewId;

        @Schema(type = "long", example = "1", description = "전시회 id를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "string", example = "뎁스 개인전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "string", example = "http://www..", description = "전시회 포스터 이미지 url을 출력합니다.")
        private String exhibitionImgUrl;

        @Schema(type = "string", example = "멋진 전시였다.", description = "감상평을 출력합니다.")
        private String contents;
    }

    @Data
    @Builder
    public static class UserReviewListRes {

        @Schema(type = "integer", example = "24", description = "내가 남긴 감상평 개수를 출력합니다.")
        private Integer numReview;

        private List<UserReviewRes> data;
    }
}
