package depth.jeonsilog.domain.interest.presentation;

import depth.jeonsilog.domain.interest.application.InterestService;
import depth.jeonsilog.domain.interest.dto.InterestResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "User 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interest")
public class InterestController {

    private final InterestService interestService;

    @Operation(summary = "즐겨찾기 등록", description = "전시회 id를 이용하여 즐겨찾기를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = InterestResponseDto.InterestRes.class))}),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{exhibitionId}")
    public ResponseEntity<?> registerInterest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "전시회 id를 입력해주세요.", required = true) @PathVariable Long exhibitionId
    ) {
        return interestService.registerInterest(userPrincipal, exhibitionId);
    }

    @Operation(summary = "즐겨찾기 해제", description = "전시회 id를 이용하여 즐겨찾기를 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "해제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<?> deleteInterest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "전시회 id를 입력해주세요.", required = true) @PathVariable Long exhibitionId
    ) {
        return interestService.deleteInterest(userPrincipal, exhibitionId);
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "Access Token을 이용하여 즐겨찾기 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = InterestResponseDto.InterestListRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> deleteInterest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return interestService.getInterestList(userPrincipal);
    }
}
