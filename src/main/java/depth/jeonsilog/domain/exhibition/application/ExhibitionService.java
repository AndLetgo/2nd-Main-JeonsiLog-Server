package depth.jeonsilog.domain.exhibition.application;

import depth.jeonsilog.domain.exhibition.converter.ExhibitionConverter;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
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
import depth.jeonsilog.domain.s3.application.S3Uploader;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final InterestRepository interestRepository;
    private final RatingRepository ratingRepository;

    private final UserService userService;
    private final S3Uploader s3Uploader;

    private static final String DIRNAME = "exhibition_img";

    // Description : 전시회 목록 조회 - 요즘 뜨는 전시
    public ResponseEntity<?> findRecentlyExhibitionList(Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(
                Sort.Order.asc("sequence"),
                Sort.Order.asc("createdDate")
        ));

        Slice<Exhibition> exhibitionPage = exhibitionRepository.findSliceBy(pageRequest);
        List<Exhibition> exhibitions = exhibitionPage.getContent();

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = ExhibitionConverter.toExhibitionListRes(exhibitions, placeInfoResList);
        boolean hasNextPage = exhibitionPage.hasNext();
        ExhibitionResponseDto.ExhibitionResListWithPaging exhibitionResListWithPaging = ExhibitionConverter.toExhibitionResListWithPaging(hasNextPage, exhibitionResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionResListWithPaging);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시회 목록 조회 - 다채로운 예술의 향연
    public ResponseEntity<?> findColorfulExhibitionList() {

        List<Exhibition> exhibitions = exhibitionRepository.findExhibitionsByAddressContainingKeyword("서울특별시 종로구");
        List<Exhibition> exhibitionsInSeoul = exhibitionRepository.findExhibitionsByNameContainingKeyword("국립현대미술관 서울관");
        Exhibition exhibitionInSeoul;
        List<Exhibition> exhibitionsInDaelim = exhibitionRepository.findExhibitionsByNameContainingKeyword("대림미술관");
        Exhibition exhibitionInDaelim;
        List<Exhibition> exhibitionsInIlmin = exhibitionRepository.findExhibitionsByNameContainingKeyword("일민미술관");
        Exhibition exhibitionInIlmin;

        List<Exhibition> exhibitionList = new ArrayList<>();

        int count = 10;
        if (!exhibitionsInSeoul.isEmpty()) {
            exhibitionInSeoul = exhibitionsInSeoul.get((int)(Math.random() * exhibitionsInSeoul.size()));
            exhibitionList.add(exhibitionInSeoul);
            count--;
        }
        if (!exhibitionsInDaelim.isEmpty()) {
            exhibitionInDaelim = exhibitionsInDaelim.get((int)(Math.random() * exhibitionsInDaelim.size()));
            exhibitionList.add(exhibitionInDaelim);
            count--;
        }
        if (!exhibitionsInIlmin.isEmpty()) {
            exhibitionInIlmin = exhibitionsInIlmin.get((int)(Math.random() * exhibitionsInIlmin.size()));
            exhibitionList.add(exhibitionInIlmin);
            count--;
        }

        if (exhibitions.size() > count) {
            List<Integer> randomNum = pickRandomIndices(exhibitions, count);
            for (Integer i : randomNum) {
                exhibitionList.add(exhibitions.get(i));
            }

        } else {
            for (Exhibition exhibition : exhibitions)
                exhibitionList.add(exhibition);
        }

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitionList) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = ExhibitionConverter.toExhibitionListRes(exhibitionList, placeInfoResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionResList);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시회 목록 조회 - 곧 종료되는 전시
    public ResponseEntity<?> findEndingSoonExhibitionList() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Exhibition> findExhibitions = exhibitionRepository.findByOperatingKeyword(OperatingKeyword.ON_DISPLAY);

        LocalDate now = LocalDate.now();
        LocalDate endDate;
        List<Exhibition> exhibitionList = new ArrayList<>();
        List<Exhibition> exhibitions = new ArrayList<>();

        for (Exhibition exhibition : findExhibitions) {
            endDate = LocalDate.parse(exhibition.getStartDate(), formatter);
            LocalDate date = endDate.minusDays(14);

            if (!now.isBefore(date))
                exhibitionList.add(exhibition);
        }

        if (exhibitionList.size() > 10) {
            List<Integer> randomNum = pickRandomIndices(exhibitionList, 10);
            for (Integer i : randomNum) {
                exhibitions.add(exhibitionList.get(i));
            }

        } else {
            for (Exhibition exhibition : exhibitionList)
                exhibitions.add(exhibition);
        }

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

    // Description : 전시회 목록 조회 - 새로 시작한 전시
    public ResponseEntity<?> findNewExhibitionList() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Exhibition> findExhibitions = exhibitionRepository.findByOperatingKeyword(OperatingKeyword.ON_DISPLAY);

        LocalDate now = LocalDate.now();
        LocalDate startDate;
        List<Exhibition> exhibitionList = new ArrayList<>();
        List<Exhibition> exhibitions = new ArrayList<>();

        for (Exhibition exhibition : findExhibitions) {
            startDate = LocalDate.parse(exhibition.getStartDate(), formatter);
            LocalDate date = startDate.plusDays(7);

            if (!now.isAfter(date))
                exhibitionList.add(exhibition);
        }

        if (exhibitionList.size() > 10) {
            List<Integer> randomNum = pickRandomIndices(exhibitionList, 10);
            for (Integer i : randomNum) {
                exhibitions.add(exhibitionList.get(i));
            }

        } else {
            for (Exhibition exhibition : exhibitionList)
                exhibitions.add(exhibition);
        }

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
    public ResponseEntity<?> randomTwoExhibitions() {

        long size = exhibitionRepository.count();

        int randomId1 = (int) (Math.random() * size + 1);
        int randomId2 = (int) (Math.random() * size + 1);

        // 같으면 숫자 다시 뽑기
        while (randomId1 == randomId2)
            randomId2 = (int) (Math.random() * size + 1);

//        Exhibition randomExhibition1 = validateExhibitionById(Long.parseLong(Integer.toString(randomId1)));
//        Exhibition randomExhibition2 = validateExhibitionById(Long.parseLong(Integer.toString(randomId2)));

        Optional<Exhibition> exhibition1 = exhibitionRepository.findById(Long.parseLong(Integer.toString(randomId1)));
        Optional<Exhibition> exhibition2 = exhibitionRepository.findById(Long.parseLong(Integer.toString(randomId2)));

        while (exhibition1.isEmpty()) {
            randomId1 = (int) (Math.random() * size + 1);
            while (randomId1 == randomId2)
                randomId1 = (int) (Math.random() * size + 1);
            exhibition1 = exhibitionRepository.findById(Long.parseLong(Integer.toString(randomId1)));
        }

        while (exhibition2.isEmpty()) {
            randomId2 = (int) (Math.random() * size + 1);
            while (randomId1 == randomId2)
                randomId2 = (int) (Math.random() * size + 1);
            exhibition2 = exhibitionRepository.findById(Long.parseLong(Integer.toString(randomId2)));
        }

        Exhibition randomExhibition1 = exhibition1.get();
        Exhibition randomExhibition2 = exhibition2.get();

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
    public ResponseEntity<?> searchExhibitions(Integer page, String searchWord) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdDate"));

        Slice<Exhibition> exhibitionPage = exhibitionRepository.findSliceByNameContainingOrPlace_AddressContaining(pageRequest, searchWord, searchWord);
        List<Exhibition> exhibitions = exhibitionPage.getContent();

        DefaultAssert.isTrue(!exhibitions.isEmpty(), "해당 검색어를 포함한 전시회가 존재하지 않습니다.");

        List<Place> places = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            Place place = exhibition.getPlace();
            places.add(place);
        }

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = PlaceConverter.toPlaceInfoListRes(places);

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = ExhibitionConverter.toExhibitionListRes(exhibitions, placeInfoResList);
        boolean hasNextPage = exhibitionPage.hasNext();
        ExhibitionResponseDto.ExhibitionResListWithPaging exhibitionResListWithPaging = ExhibitionConverter.toExhibitionResListWithPaging(hasNextPage, exhibitionResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(exhibitionResListWithPaging);

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시회 이름만으로 전시회 목록 조회 - 관리자 페이지, 포토캘린더에서 사용한다.
    public ResponseEntity<?> searchExhibitionsByName(Integer page, String exhibitionName) {

        PageRequest pageRequest = PageRequest.of(page, 10);
        Slice<Exhibition> exhibitionPage = exhibitionRepository.findSliceByNameContaining(pageRequest, exhibitionName);

        List<Exhibition> exhibitions = exhibitionPage.getContent();
        DefaultAssert.isTrue(!exhibitions.isEmpty(), "해당 검색어를 포함한 전시회가 존재하지 않습니다.");

        List<ExhibitionResponseDto.SearchExhibitionByNameRes> exhibitionResList = ExhibitionConverter.toSearchByNameRes(exhibitions);
        boolean hasNextPage = exhibitionPage.hasNext();
        ExhibitionResponseDto.SearchExhibitionByNameResListWithPaging searchExhibitionByNameResListWithPaging = ExhibitionConverter.toSearchExhibitionByNameResListWithPaging(hasNextPage, exhibitionResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(searchExhibitionByNameResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 전시회 상세 정보 수정
    @Transactional
    public ResponseEntity<?> updateExhibitionDetail(UserPrincipal userPrincipal, ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq, MultipartFile img) throws IOException {

        User user = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(user.getRole().equals(Role.ADMIN), "관리자만 수정할 수 있습니다.");

        Exhibition exhibition = validateExhibitionById(updateExhibitionDetailReq.getExhibitionId());

        String storedFileName = null;

        if (updateExhibitionDetailReq.getIsImageChange()) { // 이미지를 변경하는 경우
            if (img != null) { // 이미지 삭제가 아닌 이미지를 변경하거나 추가하는 경우
                storedFileName = s3Uploader.upload(img, DIRNAME);
            }
            // 기존 포스터 이미지가 s3에 있으면, 이미지 삭제 / 없으면(OPEN API 포스터 이미지 or NULL의 경우) 말고
            s3Uploader.deleteImage(DIRNAME, exhibition.getImageUrl());
        }

        exhibition.updateExhibitionDetail(updateExhibitionDetailReq, storedFileName);

        Place place = exhibition.getPlace();
        place.updatePlaceWithExhibitionDetail(updateExhibitionDetailReq.getUpdatePlaceInfo());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("전시회 및 전시공간 정보를 수정했습니다.").build());

        return ResponseEntity.ok(apiResponse);

    }

    // Description: 관리자 페이지 전시회 sequence 수정
    @Transactional
    public ResponseEntity<?> updateExhibitionSequence(UserPrincipal userPrincipal, ExhibitionRequestDto.UpdateExhibitionSequenceList updateSequenceReq) {

        User findUser = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(findUser.getRole() == Role.ADMIN, "관리자만 전시회 순서를 변경할 수 있습니다.");

        List<ExhibitionRequestDto.UpdateExhibitionSequence> updateExhibitionSequenceList = updateSequenceReq.getUpdateSequenceInfo();
        int size = updateExhibitionSequenceList.size();
        for (int i = 0; i < size; i++) {
            ExhibitionRequestDto.UpdateExhibitionSequence updateExhibitionSequence = updateExhibitionSequenceList.get(i);
            Optional<Exhibition> exhibition = exhibitionRepository.findById(updateExhibitionSequence.getExhibitionId());
            DefaultAssert.isTrue(exhibition.isPresent(), "전시회 정보가 올바르지 않습니다.");

            Optional<Exhibition> duplicatedExhibition = exhibitionRepository.findBySequence(updateExhibitionSequence.getSequence());
            duplicatedExhibition.ifPresent(value -> value.updateSequence(11));  // 해당 sequence를 가지고 있던 기존의 전시회는 11번으로 순서를 변경한다.

            Exhibition findExhibition = exhibition.get();
            findExhibition.updateSequence(updateExhibitionSequence.getSequence());
        }
        // sequence 삭제 로직
        for (int i = size; i < 10 ;i++) {
            Optional<Exhibition> tempExhibition = exhibitionRepository.findBySequence(i + 1);
            if (tempExhibition.isPresent()) {
                tempExhibition.get().updateSequence(11);
            } else {
                break;
            }
        }

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("전시회 순서를 변경했습니다.").build());

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

    // Description : 랜덤 뽑기
    public List<Integer> pickRandomIndices(List<Exhibition> exhibitionList, int count) {
        Random random = new Random();
        int exhibitionSize = exhibitionList.size();

        // 10개의 무작위 인덱스를 생성하고, 중복을 피하기 위해 distinct()를 사용
        List<Integer> randomIndices = IntStream.generate(() -> random.nextInt(exhibitionSize))
                .distinct()
                .limit(count)
                .boxed()
                .collect(Collectors.toList());

        return randomIndices;
    }

}
