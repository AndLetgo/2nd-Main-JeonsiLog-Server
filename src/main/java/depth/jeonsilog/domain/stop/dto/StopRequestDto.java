package depth.jeonsilog.domain.stop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class StopRequestDto {

    @Getter
    public static class StopUserReq {

        @Schema(type = "Long", example = "1", description = "유저 ID 입니다.")
        private Long userId;

        @Schema(type = "String", example = "부적절한 언행 사용", description = "정지 사유입니다.")
        private String reason;
    }
}
