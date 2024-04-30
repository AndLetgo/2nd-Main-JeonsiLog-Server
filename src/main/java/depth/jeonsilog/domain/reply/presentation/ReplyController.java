package depth.jeonsilog.domain.reply.presentation;

import depth.jeonsilog.domain.reply.application.ReplyService;
import depth.jeonsilog.domain.reply.dto.ReplyRequestDto;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Reply API", description = "Reply 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    private final ReplyService replyService;

    // Description : 댓글 목록 조회
    @Operation(summary = "댓글 목록 조회", description = "감상평에 대한 댓글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReplyResponseDto.ReplyResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> findReplyList(
            @Parameter(description = "댓글 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "감상평 Id를 입력해주세요.", required = true) @PathVariable(value = "reviewId") Long reviewId
    ) {
        return replyService.findReplyList(page, reviewId);
    }

    // Description : 댓글 작성
    @Operation(summary = "댓글 작성", description = "감상평에 대한 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createReply(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 CreateReplyReq를 참고해주세요", required = true) @RequestBody ReplyRequestDto.CreateReplyReq createReplyReq
            ) throws IOException {
        return replyService.createReply(userPrincipal, createReplyReq);
    }

    // Description : 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "감상평에 대한 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "댓글 Id를 입력해주세요.", required = true) @PathVariable(value = "replyId") Long replyId
    ) {
        return replyService.deleteReply(userPrincipal, replyId);
    }

    // Description : 댓글 존재 여부 조회
    @Operation(summary = "댓글 존재 여부 조회", description = "댓글 존재 여부를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReplyResponseDto.ExistReplyRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{replyId}")
    public ResponseEntity<?> existReply(
            @Parameter(description = "댓글 Id를 입력해주세요.", required = true) @PathVariable(value = "replyId") Long replyId
    ) {
        return replyService.existReply(replyId);
    }
}
