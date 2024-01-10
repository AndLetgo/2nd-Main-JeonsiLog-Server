package depth.jeonsilog.domain.report.presentation;

import depth.jeonsilog.domain.report.application.ReportService;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.domain.report.dto.ReportResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Report API", description = "Report 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    // Description : 신고
    @Operation(summary = "감상평, 댓글 혹은 포스터 등록 알림 신고", description = "감상평, 댓글 혹은 포스터 등록 알림 신고입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "신고 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> report(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 ReportReq를 참고해주세요", required = true) @RequestBody ReportRequestDto.ReportReq reportReq
    ) {
        return reportService.report(userPrincipal, reportReq);
    }

    // Description : 신고 내역 조회
    @Operation(summary = "신고 내역 조회", description = "신고 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponseDto.ReportResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findReportList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "신고 내역을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page
    ) {
        return reportService.findReportList(page, userPrincipal);
    }

    // Description : 신고 확인
    @Operation(summary = "신고 확인", description = "신고 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "확인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "확인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/check/{reportId}")
    public ResponseEntity<?> checkReport(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "신고 Id를 입력해주세요.", required = true) @PathVariable(value = "reportId") Long reportId
    ) {
        return reportService.checkReport(userPrincipal, reportId);
    }

}
