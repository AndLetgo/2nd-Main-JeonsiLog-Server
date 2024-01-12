package depth.jeonsilog.domain.rating.presentation;

import depth.jeonsilog.domain.rating.application.RatingService;
import depth.jeonsilog.domain.rating.dto.RatingRequestDto;
import depth.jeonsilog.domain.rating.dto.RatingResponseDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Rating API", description = "Rating 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "별점 등록", description = "전시회 id를 이용하여 별점을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> registerInterest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 RatingReq를 참고해주세요.", required = true) @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
            ) throws IOException {
        return ratingService.registerRating(userPrincipal, ratingReq);
    }

    @Operation(summary = "별점 수정", description = "전시회 id를 이용하여 별점을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping
    public ResponseEntity<?> updateRating(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 RatingReq를 참고해주세요.", required = true) @Valid @RequestBody RatingRequestDto.RatingReq ratingReq
    ) {
        return ratingService.updateRating(userPrincipal, ratingReq);
    }

    @Operation(summary = "별점 삭제", description = "전시회 id를 이용하여 별점을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<?> deleteRating(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "전시회 id를 입력해주세요.", required = true) @PathVariable Long exhibitionId
    ) {
        return ratingService.deleteRating(userPrincipal, exhibitionId);
    }

    @Operation(summary = "나의 별점 목록 조회", description = "Access Token을 이용하여 나의 별점 목록을 조회합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponseDto.RatingListRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> getMyRatingList(
            @Parameter(description = "별점 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return ratingService.getMyRatingList(page, userPrincipal);
    }

    @Operation(summary = "타 유저의 별점 목록 조회", description = "User id를 이용하여 타 유저의 별점 목록을 조회합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponseDto.RatingListRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserRatingList(
            @Parameter(description = "별점 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "User id를 입력해주세요.", required = true) @PathVariable Long userId
    ) {
        return ratingService.getUserRatingList(page, userPrincipal, userId);
    }
}
