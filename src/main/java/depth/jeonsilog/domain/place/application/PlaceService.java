package depth.jeonsilog.domain.place.application;

import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.converter.PlaceConverter;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final ExhibitionRepository exhibitionRepository;


    // Description : 전시 공간의 전시회 목록 조회
    public ResponseEntity<?> findExhibitionListInPlace(Integer page, Long placeId) {

        Place place = validatePlaceById(placeId);

        // 일단 등록(저장) 순서로 정렬 / 1페이지 당 10개
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
        Slice<Exhibition> exhibitionPage = exhibitionRepository.findSliceByPlace(pageRequest, place);
        List<Exhibition> exhibitions = exhibitionPage.getContent();

        DefaultAssert.isTrue(!exhibitions.isEmpty(), "해당 전시 공간에서의 전시회가 존재하지 않습니다.");

        List<ExhibitionResponseDto.ExhibitionInPlaceRes> exhibitionListInPlaceRes = ExhibitionConverter.toExhibitionListInPlaceRes(exhibitions);
        boolean hasNextPage = exhibitionPage.hasNext();
        ExhibitionResponseDto.ExhibitionInPlaceResWithPaging exhibitionInPlaceResWithPaging = ExhibitionConverter.toExhibitionInPlaceResWithPaging(hasNextPage, exhibitionListInPlaceRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionInPlaceResWithPaging);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 검색어를 포함한 전시 공간 목록 조회
    public ResponseEntity<?> searchPlaces(Integer page, String searchWord) {

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "createdDate"));
        Slice<Place> placePage = placeRepository.findSliceByNameContainingOrAddressContaining(pageRequest, searchWord, searchWord);
        List<Place> places = placePage.getContent();

        DefaultAssert.isTrue(!places.isEmpty(), "해당 검색어를 포함한 전시 공간이 존재하지 않습니다.");

        List<PlaceResponseDto.SearchPlaceRes> searchPlaceResList = PlaceConverter.toSearchPlaceListRes(places);
        boolean hasNextPage = placePage.hasNext();
        PlaceResponseDto.SearchPlaceResWithPaging searchPlaceResWithPaging = PlaceConverter.toSearchPlaceResWithPaging(hasNextPage, searchPlaceResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(searchPlaceResWithPaging);

        return ResponseEntity.ok(apiResponse);
    }

    public Place validatePlaceById(Long placeId) {
        Optional<Place> place = placeRepository.findById(placeId);
        DefaultAssert.isTrue(place.isPresent(), "전시 공간 정보가 올바르지 않습니다.");
        return place.get();
    }


}
