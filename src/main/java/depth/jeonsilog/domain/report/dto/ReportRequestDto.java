package depth.jeonsilog.domain.report.dto;

import depth.jeonsilog.domain.report.domain.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class ReportRequestDto {

    @Data
    public static class ReportReq {

        @Schema(type = "ReportType", example = "REVIEW", description = "신고 타입입니다. EXHIBITION, REVIEW, REPLY 中 1")
        private ReportType reportType;

        @Schema(type = "Long", example = "1", description = "신고된 감상평, 댓글 혹은 전시회의 id입니다.")
        private Long reportedId;

    }
}
