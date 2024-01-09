package depth.jeonsilog.domain.review.presentation;

import depth.jeonsilog.domain.rating.dto.RatingResponseDto;
import depth.jeonsilog.domain.review.application.ReviewService;
import depth.jeonsilog.domain.review.dto.ReviewRequestDto;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review API", description = "Review 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "감상평 작성", description = "Access Token, 전시회 id를 이용하여 감상평을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> writeReview(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 WriteReviewReq 를 참고해주세요.", required = true) @Valid @RequestBody ReviewRequestDto.WriteReviewReq writeReviewReq
            ) {
        return reviewService.writeReview(userPrincipal, writeReviewReq);
    }

    @Operation(summary = "감상평 삭제", description = "Access Token, 감상평 id를 이용하여 감상평을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Review id를 입력해주세요.", required = true) @PathVariable Long reviewId
    ) {
        return reviewService.deleteReview(userPrincipal, reviewId);
    }

    @Operation(summary = "전시회의 감상평 조회", description = "Access Token, 전시회 id를 이용하여 감상평을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDto.ReviewListRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/exhibition/{exhibitionId}")
    public ResponseEntity<?> getReviewList(
            @Parameter(description = "감상평 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Exhibition id를 입력해주세요.", required = true) @PathVariable Long exhibitionId
    ) {
        return reviewService.getReviewList(page, exhibitionId);
    }

    @Operation(summary = "나의 감상평 조회", description = "Access Token을 이용하여 나의 감상평을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.UserReviewListRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> getMyReviewList(
            @Parameter(description = "감상평 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return reviewService.getMyReviewList(page, userPrincipal);
    }

    @Operation(summary = "타 유저의 감상평 조회", description = "유저 id를 이용하여 타 유저의 감상평을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.UserReviewListRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserReviewList(
            @Parameter(description = "감상평 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "유저 id를 입력해주세요.", required = true) @PathVariable Long userId
    ) {
        return reviewService.getUserReviewList(page, userId);
    }

    @Operation(summary = "해당 전시회에 감상평을 작성했는지 체크", description = "전시회 ID와 Access Token을 이용해서 해당 전시회에 작성한 감상평이 있는지 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponseDto.CheckIsWriteRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/check/{exhibitionId}")
    public ResponseEntity<?> getUserReviewList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "전시회 id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return reviewService.checkIsWrite(userPrincipal, exhibitionId);
    }
}
