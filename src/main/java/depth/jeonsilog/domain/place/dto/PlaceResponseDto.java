package depth.jeonsilog.domain.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

    @Data
    @Builder
    public static class PlaceInfoResWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<PlaceResponseDto.PlaceInfoRes> data;

    }

}
