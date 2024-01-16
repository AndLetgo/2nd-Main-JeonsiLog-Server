package depth.jeonsilog.domain.openApi.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SaveService {

    private final ExhibitionService exhibitionService;

    private final ExhibitionRepository exhibitionRepository;

    private final PlaceRepository placeRepository;

    @Transactional
    public ResponseEntity<?> saveExhibitionAndPlace() {

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

        /**
         * Description : exhibition list 가져오는 api 호출 - count++ 파라미터로 호출
         * Description : while문 속에서 exhibition 호출 - seq 파라미터로 호출
         */

        // page
        Integer count = 1;

        while (true) {
            System.out.println("=============================== num :" + count + " ===============================");

            // TODO : Exhibition
            // String 타입 XML 받아오기
            String listXml = "";
            try {
                listXml = callExhibitionListApi(count, threeMonthsAgoStr, oneMonthsAfterStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // XML To JSON
            JSONObject jsonObject = XML.toJSONObject(listXml);
            // JSON To String
            String listJsonStr = jsonObject.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터만, 즉, 내가 필드로 선언한 데이터들만 파싱할 수 있다
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ExhibitionListDTO exhibitionList = null;
            try {
                // Description :  JSON String -> ExhibitionListDTO (Object)
                exhibitionList = objectMapper.readValue(listJsonStr, ExhibitionListDTO.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

            }

            // TODO : ExhibitionDetail
            // Description : 전시회 목록의 해당 페이지의 전시회들 상세 호출 ( 한 페이지에 최대 100개 설정 )
            for (int i = 0; i < exhibitionList.getResponse().getMsgBody().getPerforList().size(); i++) {
                // String 타입 XML 받아오기
                String detailXml = "";
                try {
                    detailXml = callExhibitionDetailApi(exhibitionList.getResponse().getMsgBody().getPerforList().get(i).getSeq());
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
                    placeXml = callPlaceDetailApi(exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getPlaceSeq());
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

                    if (placeDetail.getResponse().getMsgBody().getSeq() == 0) {
                        placeDetail.getResponse().getMsgBody().setPlaceInfo(new PlaceDetailDTO.PlaceDetailResponseDTO.PlaceDetailMsgBodyDTO.PlaceInfo());
                    }

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                // Description : place 있는 지 확인
                String placeName = placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulName();
                String placeAddr = placeDetail.getResponse().getMsgBody().getPlaceInfo().getCulAddr();
                Optional<Place> placeByName = Optional.empty();
                Optional<Place> placeByAddr = Optional.empty();
                if (placeName != null) {
                    placeByName = placeRepository.findByName(placeName);
                }
                if (placeAddr != null) {
                    placeByAddr = placeRepository.findByAddress(placeAddr);
                }

                Place place = null;

                // If : 만약 해당 전시 공간 이름 혹은 전시 공간 주소가 같은 것이 있다면 새로이 저장 x / 기존 거 사용
                if (placeByName.isPresent()) {
                    place = placeByName.get();

                } else if (placeByAddr.isPresent()) {
                    place = placeByAddr.get();

                } else {
                    if (placeName == null || placeAddr == null) {
                        place = Place.builder()
                                .name(null)
                                .address(null)
                                .homePage(null)
                                .tel(null)
                                .build();

                        placeRepository.save(place);
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

                System.out.println("------------------------- " + exhibitionDetail.getResponse().getMsgBody().getPerforInfo().getSeq()  + " -------------------------");

            } // Exhibition Detail  for loop

            // TODO : while문 종료조건 : 마지막 page까지 호출

            Integer totalCount = exhibitionList.getResponse().getMsgBody().getTotalCount();
            Integer rows = exhibitionList.getResponse().getMsgBody().getRows();
            Integer numOfPages = (totalCount / rows) + 1;

            if (count == 1) { // count로 페이지 조절
                ApiResponse apiResponse = ApiResponse.builder()
                        .check(true)
                        .information(exhibitionList)
                        .build();

                return ResponseEntity.ok(apiResponse);
            }
            count++;
        } // while

    }

    // Description : 전시회 목록 조회 OPEN API  /  파라미터로 cPage / from, to
    public String callExhibitionListApi(Integer cPage, String from, String to) throws IOException {

        // 요청 기본 URL
        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period");

        // 파라미터 추가 : 서비스 키, 날짜 범위 (from, to), 현재 페이지, 페이지 당 가져올 개수
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("from","UTF-8") + "=" + URLEncoder.encode("20231008", "UTF-8")); /**/
        urlBuilder.append("&" + URLEncoder.encode("from","UTF-8") + "=" + URLEncoder.encode(from, "UTF-8")); /**/
//        urlBuilder.append("&" + URLEncoder.encode("to","UTF-8") + "=" + URLEncoder.encode("20240208", "UTF-8")); /**/
        urlBuilder.append("&" + URLEncoder.encode("to","UTF-8") + "=" + URLEncoder.encode(to, "UTF-8")); /**/

        urlBuilder.append("&" + URLEncoder.encode("cPage","UTF-8") + "=" + URLEncoder.encode(cPage.toString(), "UTF-8")); // 요청 페이지
        urlBuilder.append("&" + URLEncoder.encode("rows","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); // 페이지 당 가져올 개수

        // URL 생성 후 연결
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 메소드 및 타입 정의
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 성공
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else { // 실패
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        // 연결 종료
        rd.close();
        conn.disconnect();

        // Return 값은 XML -> String
        return sb.toString();
    }


    // Description : 전시회 상세 정보 조회 OPEN API  /  파라미터로 seq
    public String callExhibitionDetailApi(Integer seq) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/d/"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("seq","UTF-8") + "=" + URLEncoder.encode(seq.toString(), "UTF-8")); // 전시회 번호 261752, 246264 ...

        // URL 생성 후 연결
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 메소드 및 타입 정의
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 성공
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else { // 실패
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        // 연결 종료
        rd.close();
        conn.disconnect();

        // Return 값은 XML -> String
        return sb.toString();
    }

    // Description : 전시공간 상세 정보 조회 OPEN API
    public String callPlaceDetailApi(Integer seq) throws IOException {


        StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/cultureartspaces/d/"); /*URL*/

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=6Ga8a1AdpULm31JfcyXxuDvpbDNvSy7AkVUa%2FjvlCpzW%2FtrLitTBq%2FAlbWFJ8YDsZpBeZcdnMdhJzLBl%2ByTxmQ%3D%3D"); /*Service Key*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=vpM9dG1gBFa1nJ%2FSaFhLRPJyOkMEW8GFDsEeAnNnOoeO2EvHhEBS1zzV7KLLRGD2oMOjj8VOmedb1buxQTUUuA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("seq","UTF-8") + "=" + URLEncoder.encode(seq.toString(), "UTF-8")); // 전시공간 seq

        // URL 생성 후 연결
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 메소드 및 타입 정의
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // 성공
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else { // 실패
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        // 연결 종료
        rd.close();
        conn.disconnect();

        // Return 값은 XML -> String
        return sb.toString();
    }
}
