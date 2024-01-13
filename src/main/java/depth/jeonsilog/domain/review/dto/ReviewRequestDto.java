package depth.jeonsilog.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class ReviewRequestDto {

    @Data
    public static class WriteReviewReq {

        @Schema(type = "long", example = "1", description = "전시회 id입니다.")
        @NotNull(message = "전시회 id를 입력해야 합니다.")
        private Long exhibitionId;

        @Schema(type = "string", example = "내 마음을 울리는 전시였다.", description = "감상평입니다.")
        @Size(max = 1000, message = "감상평은 공백 포함 1000자까지 입력 가능합니다.")
        private String contents;
    }

    @Data
    public static class UpdateReviewReq {

        @Schema(type = "long", example = "1", description = "감상평 id입니다.")
        @NotNull(message = "감상평 id를 입력해야 합니다.")
        private Long reviewId;

        @Schema(type = "string", example = "무한 우주에 순간의 빛일지라도", description = "감상평입니다.")
        @Size(max = 1000, message = "감상평은 공백 포함 1000자까지 입력 가능합니다.")
        private String contents;
    }
}
