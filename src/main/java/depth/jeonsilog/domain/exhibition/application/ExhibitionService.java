package depth.jeonsilog.domain.exhibition.application;

import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.converter.PlaceConverter;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    // TODO : 전체적으로 이미지에 대한 부분은 다시 처리해야 함
    // TODO : 이미지 테이블 없애고 이미지 url만 전시회 테이블에 넣기
    
    // Description : 전시회 목록 조회
    // TODO : 페이징
    public ResponseEntity<?> findExhibitionList() {

        // TODO : sequence != 0 등으로 구하면 될 듯
        // TODO : 이미지, 전시회 이름, 전시공간 이름, 전시공간 주소, 키워드

        // TODO : 페이징 구현 필요
        return ResponseEntity.badRequest().body("구현해라");
    }

    // Description : 전시회 상세 정보 조회 (feat. id)
    public ResponseEntity<?> findExhibition(Long exhibitionId) {

        Exhibition exhibition = validateExhibitionById(exhibitionId);

        Place place = exhibition.getPlace();
        PlaceResponseDto.PlaceRes placeRes = PlaceConverter.toPlaceRes(place);

        ExhibitionResponseDto.ExhibitionRes exhibitionRes = ExhibitionConverter.toExhibitionRes(exhibition, placeRes);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(exhibitionRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 랜덤 전시회 2개 조회
    // TODO : 이건 전체 중 2개만 조회하면 되는 것으로 일단 생각했음
    public ResponseEntity<?> randomTwoExhibitions() {

        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        // IF jpa count가 있음 !!

        int size = exhibitions.size();
        Integer randomId1 = (int) (Math.random() * size + 1);
        Integer randomId2 = (int) (Math.random() * size + 1);

        // 같으면 숫자 다시 뽑기
        while (randomId1 == randomId2)
            randomId2 = (int) (Math.random() * size + 1);

        Exhibition randomExhibition1 = validateExhibitionById(Long.parseLong(Integer.toString(randomId1)));
        Exhibition randomExhibition2 = validateExhibitionById(Long.parseLong(Integer.toString(randomId2)));

        List<ExhibitionResponseDto.RandomExhibitionRes> randomExhibitionResList = new ArrayList<>();

        // random 1
        ExhibitionResponseDto.RandomExhibitionRes randomExhibitionRes = ExhibitionConverter.toRandomExhibitionRes(randomExhibition1);
        randomExhibitionResList.add(randomExhibitionRes);

        // random2
        randomExhibitionRes = ExhibitionConverter.toRandomExhibitionRes(randomExhibition2);
        randomExhibitionResList.add(randomExhibitionRes);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(randomExhibitionResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 검색어를 포함한 전시회 목록 조회
    public ResponseEntity<?> searchExhibitions(String searchWord) {

        List<Exhibition> exhibitions = exhibitionRepository.findByNameContaining(searchWord);
        // 이렇게 써도 될지 .. ?
        DefaultAssert.isTrue(!exhibitions.isEmpty(), "해당 검색어를 포함한 전시회가 존재하지 않습니다.");

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.SearchExhibitionRes> searchExhibitionListRes = ExhibitionConverter.toSearchExhibitionListRes(exhibitions, placeInfoResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(searchExhibitionListRes)
                .build();

        return ResponseEntity.ok(searchExhibitionListRes);

    }

    // Description : 전시회 상세 정보 수정
    // TODO : 전시회 이미지 필요
//    public ResponseEntity<?> updateExhibitionDetail(Long exhibitionId) {
//
//        Exhibition exhibition = validateExhibitionById(exhibitionId);
//
//        /**
//         * 이름
//         * 키워드
//         * 키워드 추가
//         * 세부정보
//         * - 전시공간 주소
//         * - 전시공간 운영시간
//         * - 전시공간 휴관일
//         * - 전시공간 전화번호
//         * - 전시공간 홈페이지 링크
//         * 전시회 정보
//         */
//
//
//    }

    public Exhibition validateExhibitionById(Long exhibitionId) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 정보가 올바르지 않습니다.");
        return exhibition.get();
    }

}
