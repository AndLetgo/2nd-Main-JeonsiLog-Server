package depth.jeonsilog.domain.exhibition.presentation;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.global.payload.ErrorResponse;
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

@Tag(name = "Exhibition API", description = "Exhibition 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    // Description : 전시회 목록 조회
//    @Operation(summary = "전시회 목록 조회", description = "전시회 목록을 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionRes.class))}),
//            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
    @GetMapping
    public ResponseEntity<?> findExhibitionList() {
        return exhibitionService.findExhibitionList();
    }

    // Description : 전시회 상세 정보 조회
    @Operation(summary = "전시회 상세 정보 조회", description = "전시회 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{exhibitionId}")
    public ResponseEntity<?> findExhibition(
            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId
    ) {
        return exhibitionService.findExhibition(exhibitionId);
    }

    // Description : 랜덤 전시회 2개 조회
    @Operation(summary = "랜덤 전시회 2개 조회", description = "랜덤 전시회를 2개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.RandomExhibitionRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    // TODO : 이건 전체 중 2개만 조회하면 되는 것으로 일단 생각했음
    @GetMapping("/random")
    public ResponseEntity<?> randomTwoExhibitions() {
        return exhibitionService.randomTwoExhibitions();
    }

    // Description : 검색어를 포함한 전시회 목록 조회
    @Operation(summary = "검색어를 포함한 전시회 목록 조회", description = "검색어를 포함한 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.SearchExhibitionRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchExhibitions(
            @Parameter(description = "검색어를 입력해주세요.", required = true) @PathVariable(value = "searchWord") String searchWord
    ) {
        return exhibitionService.searchExhibitions(searchWord);
    }

    // Description : 전시회 상세 정보 수정
//    @PatchMapping("/{exhibitionId}")
//    public ResponseEntity<?> updateExhibitionDetail(
//            @Parameter(description = "Exhibition Id를 입력해주세요.", required = true) @PathVariable(value = "exhibitionId") Long exhibitionId,
//            @Parameter(description = "Schemas의 updateExhibitionDetailReq를 참고해주세요", required = true) @RequestBody
//
//    ) {
//        return exhibitionService.updateExhibitionDetail(exhibitionId);
//    }

    // Description : 전시회 id로 전시회 포스터 조회

    // TODO : Description : 키워드 추가 입력 - 보류로 되어있긴 함


}
