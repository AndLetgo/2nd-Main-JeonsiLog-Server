package depth.jeonsilog.domain.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RatingResponseDto {

    @Data
    @Builder
    public static class RatingRes {

        @Schema(type = "long", example = "1", description = "별점 id를 출력합니다.")
        private Long ratingId;

        @Schema(type = "long", example = "1", description = "전시회 id를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "string", example = "뎁스 개인전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "double", example = "4.5", description = "별점을 출력합니다.")
        private Double rate;
    }

    @Data
    @Builder
    public static class RatingListRes {

        @Schema(type = "integer", example = "15", description = "내가 남긴 별점 개수를 출력합니다.")
        private Integer numRating;

        private List<RatingRes> data;
    }
}
