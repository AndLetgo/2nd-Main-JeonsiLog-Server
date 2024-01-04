package depth.jeonsilog.domain.exhibition.application;

import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.repository.InterestRepository;
import depth.jeonsilog.domain.place.converter.PlaceConverter;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final InterestRepository interestRepository;
    private final RatingRepository ratingRepository;

    private final UserService userService;

    // Description : 전시회 목록 조회
    // TODO : OK
    public ResponseEntity<?> findExhibitionList(Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(
                Sort.Order.asc("sequence"),
                Sort.Order.asc("createdDate")
        ));

        Page<Exhibition> exhibitionPage = exhibitionRepository.findAll(pageRequest);

        List<Exhibition> exhibitions = exhibitionPage.getContent();

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = ExhibitionConverter.toExhibitionListRes(exhibitions, placeInfoResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionResList);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시회 상세 정보 조회
    // TODO : OK
    public ResponseEntity<?> findExhibition(UserPrincipal userPrincipal, Long exhibitionId) {

        User user = userService.validateUserByToken(userPrincipal);
        Exhibition exhibition = validateExhibitionById(exhibitionId);

        Place place = exhibition.getPlace();
        PlaceResponseDto.PlaceRes placeRes = PlaceConverter.toPlaceRes(place);

        Optional<Interest> findInterest = interestRepository.findByUserIdAndExhibitionId(user.getId(), exhibition.getId());
        Boolean checkInterest = findInterest.isPresent();

        Optional<Rating> findRating = ratingRepository.findByUserIdAndExhibitionId(user.getId(), exhibition.getId());
        Rating rating = findRating.orElse(null);
        Double myRate = null;
        if (rating != null) {
            myRate = rating.getRate();
        }

        ExhibitionResponseDto.ExhibitionDetailRes exhibitionDetailRes = ExhibitionConverter.toExhibitionDetailRes(exhibition, placeRes, checkInterest, myRate);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionDetailRes);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 랜덤 전시회 2개 조회
    // TODO : OK
    public ResponseEntity<?> randomTwoExhibitions() {

        long size = exhibitionRepository.count();

        int randomId1 = (int) (Math.random() * size + 1);
        int randomId2 = (int) (Math.random() * size + 1);

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

        ApiResponse apiResponse = ApiResponse.toApiResponse(randomExhibitionResList);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 검색어를 포함한 전시회 목록 조회
    // TODO : 논의 후 페이징 처리 필요
    public ResponseEntity<?> searchExhibitions(Integer page, String searchWord) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdDate"));

        Page<Exhibition> exhibitionPage = exhibitionRepository.findByNameContaining(pageRequest, searchWord);

        List<Exhibition> exhibitions = exhibitionPage.getContent();
        // 이렇게 써도 될지 .. ?
        DefaultAssert.isTrue(!exhibitions.isEmpty(), "해당 검색어를 포함한 전시회가 존재하지 않습니다.");

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = ExhibitionConverter.toExhibitionListRes(exhibitions, placeInfoResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionResList);

        return ResponseEntity.ok(apiResponse);

    }

    // Description : 전시회 상세 정보 수정
    // TODO : OK
    @Transactional
    public ResponseEntity<?> updateExhibitionDetail(ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq) {

        Exhibition exhibition = validateExhibitionById(updateExhibitionDetailReq.getExhibitionId());
        exhibition.updateExhibitionDetail(updateExhibitionDetailReq);

        Place place = exhibition.getPlace();
        place.updatePlaceWithExhibitionDetail(updateExhibitionDetailReq.getUpdatePlaceInfo());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("전시회 및 전시공간 정보를 수정했습니다.").build());

        return ResponseEntity.ok(apiResponse);

    }

    // Description : 전시회 ID로 전시회 포스터 조회
    public ResponseEntity<?> findPoster(Long exhibitionId) {

        Exhibition exhibition = validateExhibitionById(exhibitionId);

        ExhibitionResponseDto.PosterRes posterRes = ExhibitionConverter.toPosterRes(exhibition);

        ApiResponse apiResponse = ApiResponse.toApiResponse(posterRes);

        return ResponseEntity.ok(apiResponse);
    }

    public Exhibition validateExhibitionById(Long exhibitionId) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 정보가 올바르지 않습니다.");
        return exhibition.get();
    }

}
