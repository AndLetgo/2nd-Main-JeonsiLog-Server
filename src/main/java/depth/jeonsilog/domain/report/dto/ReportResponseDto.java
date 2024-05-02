package depth.jeonsilog.domain.report.dto;

import depth.jeonsilog.domain.report.domain.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

public class ReportResponseDto {

    @Data
    @Builder
    public static class ReportRes {

        @Setter
        @Schema(type = "String", example = "1", description = "신고된 감상평/댓글 작성자 혹은 전시회 이름이 출력합니다.")
        private String name;

        @Schema(type = "ReportType", example = "REVIEW", description = "신고 타입을 출력합니다. EXHIBITION, REVIEW, REPLY 中 1")
        private ReportType reportType;

        @Schema(type = "Long", example = "1", description = "신고된 감상평, 댓글 혹은 전시회 ID를 출력합니다.")
        private Long reportedId;

        @Schema(type = "Long", example = "1", description = "클릭 시 이동할 페이지를 위한 ID입니다. Exhibition, Review는 Exhibition, Review ID와 같으며, Reply의 경우 clickId는 작성된 Review의 ID입니다.")
        private Long clickId;

        @Schema(type = "boolean", example = "true", description = "신고를 확인했는지 출력합니다.")
        private Boolean isChecked;

        @Schema(type = "Long", example = "7", description = "알림 카운팅입니다.")
        private Integer counting;

        public ReportRes(final String name, final ReportType reportType, final Long reportedId, final Long clickId, final Boolean isChecked, final Integer counting) {
            this.name = name;
            this.reportType = reportType;
            this.reportedId = reportedId;
            this.clickId = clickId;
            this.isChecked = isChecked;
            this.counting = counting;
        }
    }

    @Data
    @Builder
    public static class ReportResListWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<ReportResponseDto.ReportRes> data;
    }
}
