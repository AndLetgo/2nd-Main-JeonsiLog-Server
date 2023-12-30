package depth.jeonsilog.domain.report.dto;

import depth.jeonsilog.domain.report.domain.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class ReportResponseDto {

    @Data
    @Builder
    public static class ReportRes {

        @Schema(type = "Long", example = "1", description = "신고 ID를 출력합니다.")
        private Long reportId;

        @Schema(type = "String", example = "1", description = "신고된 감상평/댓글 작성자 혹은 전시회 이름이 출력합니다.")
        private String name;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "신고된 감상평/댓글 작성자 프로필 이미지 혹은 전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;

        @Schema(type = "ReportType", example = "REVIEW", description = "신고 타입을 출력합니다. EXHIBITION, REVIEW, REPLY 中 1")
        private ReportType reportType;

        @Schema(type = "Long", example = "1", description = "신고된 감상평, 댓글 혹은 전시회 ID를 출력합니다.")
        private Long reportedId;
    }
}
