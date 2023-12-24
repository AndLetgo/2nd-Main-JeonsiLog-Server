package depth.jeonsilog.domain.place.presentation;

import depth.jeonsilog.domain.place.application.PlaceService;
import io.swagger.v3.oas.annotations.Parameter;
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
    @GetMapping("/exhibition/{placeId}")
    public ResponseEntity<?> findExhibitionListInPlace(
            @Parameter(description = "전시 공간에서 진행되는 전시회 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Place Id를 입력해주세요.", required = true) @PathVariable(value = "placeId") Long placeId
    ) {
        return placeService.findExhibitionListInPlace(page, placeId);
    }

}
