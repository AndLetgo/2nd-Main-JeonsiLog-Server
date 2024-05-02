package depth.jeonsilog.domain.report.dto;

import depth.jeonsilog.domain.report.domain.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class ReportRequestDto {

    @Data
    public static class ReportReq {

        @Schema(type = "ReportType", example = "REVIEW", description = "신고 타입입니다. EXHIBITION, REVIEW, REPLY, LINK, ADDRESS, PHONE_NUMBER 中 1")
        private ReportType reportType;

        @Schema(type = "Long", example = "1", description = "신고된 감상평, 댓글, 전시회, 전시공간의 ID 입니다.")
        private Long reportedId;

    }
}
