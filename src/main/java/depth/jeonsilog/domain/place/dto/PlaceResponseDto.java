package depth.jeonsilog.domain.place.dto;

import depth.jeonsilog.domain.place.domain.ClosedDays;
import lombok.Builder;
import lombok.Data;

public class PlaceResponseDto {

    // 전시회 상세 정보 조회 시 사용될 place
    @Data
    @Builder
    public static class PlaceRes {

        // id 필요한지 ..
        private Long placeId;

        private String placeName;

        private String address;

        private String operationTime;

        private ClosedDays closedDays;

        private String tel;

        private String homePage;

        /*
        TODO :
            전시공간 이름
            전시공간 주소
            전시공간 운영시간
            전시공간 휴관일
            전시공간 전화번호
            전시공간 홈페이지 링크
         */
    }

    // 전시회 검색 시 사용될 place
    @Data
    @Builder
    public static class PlaceInfoRes {

        private Long placeId;

        private String placeName;
    }

}
