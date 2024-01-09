package depth.jeonsilog.domain.openApi.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.openApi.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.domain.openApi.dto.API.ExhibitionListDTO;
import depth.jeonsilog.domain.openApi.dto.API.PlaceDetailDTO;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AddService {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceRepository placeRepository;

    private final SaveService saveService;

    @Transactional
    public ResponseEntity<?> addExhibitionAndPlace() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 현재 날짜
        LocalDate today = LocalDate.now();
        // 3개월 전의 날짜 계산
        LocalDate threeMonthsAgo = today.minusMonths(3);
        // 1개월 후의 날짜 계산
        LocalDate oneMonthsAfter = today.plusMonths(1);

        String todayStr = today.format(formatter);
        String threeMonthsAgoStr = threeMonthsAgo.format(formatter);
        String oneMonthsAfterStr = oneMonthsAfter.format(formatter);

        // page
        Integer page = 1;

        while (true) {

            // TODO : Exhibition
            // String 타입 XML 받아오기
            String listXml = "";
            try {
                listXml = saveService.callExhibitionListApi(page, threeMonthsAgoStr, oneMonthsAfterStr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // XML To JSON
            JSONObject jsonObject = XML.toJSONObject(listXml);
            // JSON To String
            String listJsonStr = jsonObject.toString();
            // Jackson Objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터만, 즉, 내가 필드로 선언한 데이터들만 파싱할 수 있다
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ExhibitionListDTO exhibitionList = null;
            try {
                // Description : JSON String -> ExhibitionListDTO (Object)
                //   여기서 전시회 seq를 가진 녀석을 List로 받음 !! (page 단위로)
                exhibitionList = objectMapper.readValue(listJsonStr, ExhibitionListDTO.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < exhibitionList.getResponse().getMsgBody().getPerforList().size(); i++) {

                Optional<Exhibition> byExhibitionSeq = exhibitionRepository.findByExhibitionSeq(exhibitionList.getResponse().getMsgBody().getPerforList().get(i).getSeq());

                if (byExhibitionSeq.isEmpty()) { // If : 새로운 전시회라면!! -> 전시회 상세 정보 조회 후 저장(전시 공간과 함께)
                    // String 타입 XML 받아오기
                    String detailXml = "";
                    try {
                        detailXml = saveService.callExhibitionDetailApi(exhibitionList.getResponse().getMsgBody().getPerforList().get(i).getSeq());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // XML To JSON
                    jsonObject = XML.toJSONObject(detailXml);
                    // JSON To String
                    String detailJsonStr = jsonObject.toString();
                    // Jackson Objectmapper 객체 생성
                    objectMapper = new ObjectMapper();
                    // JSON의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터만, 즉, 내가 필드로 선언한 데이터들만 파싱할 수 있다
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    ExhibitionDetailDTO exhibitionDetail = null;
                    try {
                        // JSON String -> ExhibitionListDTO (Object)
                        exhibitionDetail = objectMapper.readValue(detailJsonStr, ExhibitionDetailDTO.class);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    // TODO : Place of ExhibitionDetail
                    // String 타입 XML 받아오기
                    String placeXml = "";
                    try {
                        placeXml = saveService.callPlaceDetailApi(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPlaceSeq());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // XML To JSON
                    jsonObject = XML.toJSONObject(placeXml);
                    // JSON To String
                    String placeJsonStr = jsonObject.toString();
                    // Jackson Objectmapper 객체 생성
                    objectMapper = new ObjectMapper();
                    // JSON의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터만, 즉, 내가 필드로 선언한 데이터들만 파싱할 수 있다
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    PlaceDetailDTO placeDetail = null;
                    try {
                        // JSON String -> ExhibitionListDTO (Object)
                        placeDetail = objectMapper.readValue(placeJsonStr, PlaceDetailDTO.class);

                        if (placeDetail.getResponse().getMsgBody().getSeq() == 0) { // 빈 전시 공간
                            placeDetail.getResponse().getMsgBody().setPlaceInfo(new PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo());
                        }

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    // Description : place 있는 지 확인
                    String placeName = placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulName();
                    String placeAddr = placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulAddr();

                    Optional<Place> placeByName = placeRepository.findByName(placeName);
                    Optional<Place> placeByAddr = placeRepository.findByAddress(placeAddr);

                    Place place = null;

                    // If : 만약 해당 전시 공간 이름 혹은 전시 공간 주소가 같은 것이 있다면 새로이 저장 x / 기존 거 사용
                    if (placeByName.isPresent()) {
                        place = placeByName.get();

                    } else if (placeByAddr.isPresent()) {
                        place = placeByAddr.get();

                    } else {
                        // Description : Place 최초 저장
                        place = Place.builder()
                                .name(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulName())
                                .address(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulAddr())
                                .homePage(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulHomeUrl())
                                .tel(placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulTel())
                                .build();

                        placeRepository.save(place);
                    }

                    PriceKeyword priceKeyword = null;

                    // Description : Exhibition 저장

                    // TODO : 유료 / 무료 키워드
                    if (exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPrice().equals("무료") || exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPrice().equals("전석무료")) {
                        priceKeyword = PriceKeyword.FREE;
                    } else {
                        priceKeyword = PriceKeyword.PAY;
                    }

                    /**
                     * TODO : 시작 전 / 전시 중 키워드
                     *  - 시작 전 : startDate가 now보다 더 미래
                     *  - 전시 중 : now가 startDate보다 더 미래 && endDate가 now보다 더 미래
                     */

                    LocalDate now = LocalDate.now();
                    LocalDate startDate = LocalDate.parse(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getStartDate(), formatter);
                    LocalDate endDate = LocalDate.parse(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getEndDate(), formatter);

                    OperatingKeyword operatingKeyword = null;

                    if ((now.isEqual(startDate) || now.isAfter(startDate)) && (now.isEqual(endDate) || now.isBefore(endDate))) {
                        // 전시 중
                        operatingKeyword = OperatingKeyword.ON_DISPLAY;
                    } else if (now.isBefore(startDate)) {
                        // 시작 전
                        operatingKeyword = OperatingKeyword.BEFORE_DISPLAY;
                    } else {
                        // 전시 종료
                        operatingKeyword = OperatingKeyword.AFTER_DISPLAY;
                    }


                    Exhibition exhibition = Exhibition.builder()
                            .place(place)
                            .name(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getTitle())
                            .operatingKeyword(operatingKeyword)
                            .priceKeyword(priceKeyword)
                            .price(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPrice())
                            .startDate(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getStartDate())
                            .endDate(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getEndDate())
                            .information(null)
                            .rate(null)
                            .exhibitionSeq(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getSeq())
                            .imageUrl(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getImgUrl())
                            .build();

                    exhibitionRepository.save(exhibition);

                } // if new exhibition

            } // loop

            // TODO : while문 종료조건 : 마지막 page까지 호출
            Integer totalCount = exhibitionList.getResponse().getMsgBody().getTotalCount();
            Integer rows = exhibitionList.getResponse().getMsgBody().getRows();
            Integer numOfPages = (totalCount / rows) + 1;

            if (page == numOfPages) { // 페이지 조절
                ApiResponse apiResponse = ApiResponse.builder()
                        .check(true)
                        .information(exhibitionList)
                        .build();

                return ResponseEntity.ok(apiResponse);
            }
            page++;

        } // while
    }
}
