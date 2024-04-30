package depth.jeonsilog.domain.stop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class StopResponseDto {

    @Getter
    @Builder
    public static class StopUserRes {

        @Schema(type = "String", example = "부적절한 언행 사용", description = "정지 사유를 출력합니다.")
        private String reason;

        @Schema(type = "Integer", example = "6", description = "남은 정지 일수를 출력합니다..")
        private Integer remainingDays;
    }
}
