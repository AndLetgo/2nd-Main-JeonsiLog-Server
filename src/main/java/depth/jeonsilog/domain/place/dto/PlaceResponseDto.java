package depth.jeonsilog.domain.place.dto;

import depth.jeonsilog.domain.place.domain.ClosedDays;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class PlaceResponseDto {

    // 전시회 상세 정보 조회 시 사용될 place
    @Data
    @Builder
    public static class PlaceRes {

        @Schema(type = "Long", example = "1", description = "전시 공간 ID입니다.")
        private Long placeId;

        @Schema(type = "String", example = "롯데콘서트홀", description = "전시 공간 이름입니다.")
        private String placeName;

        @Schema(type = "String", example = "서울특별시 송파구 올림픽로 300 롯데월드몰 8층 롯데콘서트홀", description = "전시 공간 주소입니다.")
        private String address;

        @Schema(type = "String", example = "~~~ ~~~", description = "전시 공간 운영 시간입니다.")
        private String operationTime;

        @Schema(type = "ClosedDays", example = "MONDAY", description = "전시 공간 휴관일입니다.")
        private ClosedDays closedDays;

        @Schema(type = "String", example = "031-590-4358", description = "전시 공간 전화번호입니다.")
        private String tel;

        @Schema(type = "String", example = "\thttps://culture.nyj.go.kr/home/", description = "전시 공간 전화번호입니다.")
        private String homePage;

    }

    // 전시회 목록 조회 및 전시회 검색 시 사용될 place
    @Data
    @Builder
    public static class PlaceInfoRes {

        @Schema(type = "Long", example = "1", description = "전시 공간 ID입니다.")
        private Long placeId;

        @Schema(type = "String", example = "롯데콘서트홀", description = "전시 공간 이름입니다.")
        private String placeName;

        @Schema(type = "String", example = "서울특별시 송파구 올림픽로 300 롯데월드몰 8층 롯데콘서트홀", description = "전시 공간 주소입니다.")
        private String placeAddress;
    }

    // 검색어를 포함한 전시 공간 목록 조회
    @Data
    @Builder
    public static class SearchPlaceRes {

        @Schema(type = "Long", example = "1", description = "전시 공간 ID입니다.")
        private Long placeId;

        @Schema(type = "String", example = "롯데콘서트홀", description = "전시 공간 이름입니다.")
        private String placeName;

    }

}
