package depth.jeonsilog.domain.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class RatingRequestDto {

    @Data
    public static class RatingReq {

        @Schema(type = "long", example = "1", description = "전시회 id입니다.")
        @NotNull(message = "전시회 id를 입력해야 합니다.")
        private Long exhibitionId;

        @Schema(type = "double", example = "4.5", description="별점입니다.")
        @NotNull(message = "별점을 입력해야 합니다.")
        private Double rate;
    }
}
