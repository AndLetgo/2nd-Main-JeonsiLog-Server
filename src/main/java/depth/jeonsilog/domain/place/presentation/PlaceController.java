package depth.jeonsilog.domain.place.presentation;

import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.application.PlaceService;
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

@Tag(name = "Place API", description = "Place 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    // Description : 전시 공간의 전시회 목록 조회
    // TODO : 논의 후 페이징 처리
    @Operation(summary = "전시 공간의 전시회 목록 조회", description = "전시 공간의 전시회 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionResponseDto.ExhibitionInPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/exhibition/{placeId}")
    public ResponseEntity<?> findExhibitionListInPlace(
            @Parameter(description = "전시 공간에서 진행되는 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Place Id를 입력해주세요.", required = true) @PathVariable(value = "placeId") Long placeId
    ) {
        return placeService.findExhibitionListInPlace(page, placeId);
    }

    // Description : 검색어를 포함한 전시공간 목록 조회
    // TODO : 논의 후 페이징 처리
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchPlaces(
            @Parameter(description = "검색한 전시 공간 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @PathVariable(value = "searchWord") String searchWord
    ) {
        return placeService.searchPlaces(page, searchWord);
    }

}
