package depth.jeonsilog.domain.openApi.application;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.converter.PlaceConverter;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ModifyService {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceRepository placeRepository;

    private static final String AMP = "%amp;";
    private static final String REPLACE_AMP = "&";

    private static final String LT = "&lt;";
    private static final String REPLACE_LT = "<";

    private static final String GT = "&gt;";
    private static final String REPLACE_GT = ">";

    private static final String QUOT = "&quot;";
    private static final String REPLACE_QUOT = "\"";

    private static final String BACKTICK = "&#39;";
    private static final String REPLACE_BACKTICK = "'";

    // Description : 전시회 이름 변경 및 검증 - 꺽쇠 (&lt; ...) 등 올바르게 변경  &&  이름 null or 비어 있을 시 제거
    @Transactional
    public ResponseEntity<?> modifyExhibitionName() {

        // 조회 시 조건 걸어서 바꿀 수도 있음.. 하지만.. 이건 한 번 호출하는 것이라 중요도가 높지는 않음. 생각은 해 보면 좋을 듯
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        List<Exhibition> exhibitionList = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            String name = exhibition.getName();

            // 이름이 비어 있는 경우 삭제
            if (name == null || name.equals("") || name.equals(" ")) {
                exhibitionList.add(exhibition);
                log.info("삭제된 전시회 ID : " + exhibition.getId());
                exhibitionRepository.delete(exhibition);
                continue;
            }

            // HTML 특수 문자 포함 여부 확인 후 수정
            if (containsSpecialCharacters(name)) {
                log.info("전시회 이름 변경 전 : " + exhibition.getName());
                name = name.replace(AMP, REPLACE_AMP);
                name = name.replace(LT, REPLACE_LT);
                name = name.replace(GT, REPLACE_GT);
                name = name.replace(QUOT, REPLACE_QUOT);
                name = name.replace(BACKTICK, REPLACE_BACKTICK);
                exhibition.updateName(name);

                exhibitionList.add(exhibition);
                log.info("전시회 이름 변경 후 : " + exhibition.getName());
            }
        }
        // 이름 변경한 전시회 리스트 반환
        List<ExhibitionResponseDto.SearchExhibitionByNameRes> exhibitionByNameRes = ExhibitionConverter.toSearchByNameRes(exhibitionList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionByNameRes);
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시 공간 전화 번호 여러 개 있는 경우 하나만 저장
    @Transactional
    public ResponseEntity<?> modifyPlaceTel() {

        // 마찬가지로 조회 시 조건 걸어서 바꿀 수도 있음.. 하지만.. 이건 한 번 호출하는 것이라 중요도가 높지는 않음. 생각은 해 보면 좋을 듯
        List<Place> places = placeRepository.findAll();
        List<PlaceResponseDto.PlaceRes> placeResList = new ArrayList<>();

        for (Place place : places) {
            String tel = place.getTel();

            // 전화 번호가 비어 있는 경우 null로 통일
            if (tel.equals("") || tel.equals(" ")) {
                log.info("전화 번호가 비어 있는 전시 공간 이름: " + place.getName() + " 전화 번호 : " + place.getTel());
                place.updateTel(null);
                PlaceResponseDto.PlaceRes placeRes = PlaceConverter.toPlaceRes(place);
                placeResList.add(placeRes);
                continue;
            }

            // 전화 번호가 여러 개인 경우
            if (tel.contains(",") || tel.contains("~")) {
                log.info("전화 번호가 여러 개 있는 전시 공간 이름: " + place.getName() + " 변경 전 전화 번호 : " + place.getTel());
                tel = selectOneTel(tel);
                place.updateTel(tel);
                PlaceResponseDto.PlaceRes placeRes = PlaceConverter.toPlaceRes(place);
                placeResList.add(placeRes);
                log.info("전화 번호가 여러 개 있는 전시 공간 이름: " + place.getName() + " 변경 후 전화 번호 : " + place.getTel());
            }

        }
        ApiResponse apiResponse = ApiResponse.toApiResponse(placeResList);
        return ResponseEntity.ok(apiResponse);
    }

    private boolean containsSpecialCharacters(String name) {
        return (name.contains(AMP) ||
                name.contains(LT) ||
                name.contains(GT) ||
                name.contains(QUOT) ||
                name.contains(BACKTICK));
    }

    private String selectOneTel(String tel) {
        String result = tel;
        if (result.contains(",")) {
            result = result.substring(0, tel.indexOf(",")).trim();

        } else if (tel.contains("~")) {
            result = result.substring(0, tel.indexOf("~")).trim();

        }
        return result;
    }
}
