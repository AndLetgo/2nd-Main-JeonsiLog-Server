package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Exhibition API", description = "Exhibition 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    // Description : 전시회 목록 조회
    @Operation(summary = "전시회 목록 조회", description = "전시회 목록을 조회합니다. 관리자가 미리 설정해 둔 10개의 전시회와 그 뒤로 저장 순서대로 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findExhibitionList(
            @Parameter(description = "전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page
    ) {
        return exhibitionService.findExhibitionList(page);
    }

    // Description : 전시회 상세 정보 조회
    @Operation(summary = "전시회 상세 정보 조회", description = "전시회 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{exhibitionId}")
    public ResponseEntity<?> findExhibition(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return exhibitionService.findExhibition(userPrincipal, exhibitionId);
    }

    // Description : 랜덤 전시회 2개 조회
    @Operation(summary = "랜덤 전시회 2개 조회", description = "랜덤 전시회를 2개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExhibitionResponseDto.RandomExhibitionRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/random")
    public ResponseEntity<?> randomTwoExhibitions() {
        return exhibitionService.randomTwoExhibitions();
    }

    // Description : 검색어를 포함한 전시회 목록 조회
    // TODO : 논의 후 페이징 처리 필요
    @Operation(summary = "검색어를 포함한 전시회 목록 조회", description = "검색어를 포함한 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchExhibitions(
            @Parameter(description = "검색한 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @PathVariable(value = "searchWord") String searchWord
    ) {
        return exhibitionService.searchExhibitions(page, searchWord);
    }

    // Description : 전시회 상세 정보 수정
    @Operation(summary = "전시회 상세 정보 및 전시 공간 정보 수정", description = "전시회 상세 정보 및 전시 공간 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping
    public ResponseEntity<?> updateExhibitionDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UpdateExhibitionDetailReq와 UpdatePlaceWithExhibitionDetailReq를 참고해주세요", required = true) @RequestPart("updateExhibitionDetailReq") ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq,
            @Parameter(description = "등록할 전시회 포스터 이미지 파일을 입력해주세요.") @RequestPart(value = "img", required = false) MultipartFile img
            ) throws IOException {
        return exhibitionService.updateExhibitionDetail(userPrincipal, updateExhibitionDetailReq, img);
    }

    // Description : 전시회 id로 전시회 포스터 조회
    @Operation(summary = "전시회 포스터 조회", description = "전시회 포스터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.PosterRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/poster/{exhibitionId}")
    public ResponseEntity<?> findPoster(
            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return exhibitionService.findPoster(exhibitionId);
    }

    @Operation(summary = "전시회 이름으로 전시회 조회", description = "전시회 이름만으로 전시회를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.SearchExhibitionByNameResListWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/name/{searchWord}")
    public ResponseEntity<?> searchExhibitionByName(
            @Parameter(description = "검색한 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "전시회 이름을 입력해주세요.", required = true) @PathVariable String searchWord
    ) {
        return exhibitionService.searchExhibitionsByName(page, searchWord);
    }

    @Operation(summary = "전시회 순서 변경", description = "관리자 페이지에서 전시회 순서를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/sequence")
    public ResponseEntity<?> updateExhibitionSequence(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UpdateExhibitionSequenceList와 UpdateExhibitionSequence를 참고해주세요", required = true) @RequestBody ExhibitionRequestDto.UpdateExhibitionSequenceList requestDto
    ) {
        return exhibitionService.updateExhibitionSequence(userPrincipal, requestDto);
    }
}
