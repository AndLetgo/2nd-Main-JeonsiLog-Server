package depth.jeonsilog.domain.openApi.application;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
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

    private final ExhibitionService exhibitionService;

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

        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        List<Exhibition> exhibitionList = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            String name = exhibition.getName();

            // 이름이 비어 있는 경우 삭제
            if (name == null || name.equals("") || name.equals(" ")) {
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

    private boolean containsSpecialCharacters(String name) {
        return (name.contains(AMP) ||
                name.contains(LT) ||
                name.contains(GT) ||
                name.contains(QUOT) ||
                name.contains(BACKTICK));
    }
}
