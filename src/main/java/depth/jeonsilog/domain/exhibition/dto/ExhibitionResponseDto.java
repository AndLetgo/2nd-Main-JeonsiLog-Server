package depth.jeonsilog.domain.exhibition.dto;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class ExhibitionResponseDto {

    // 전시회 목록 조회 + 검색어를 포함한 전시회 목록 조회
    @Data
    @Builder
    public static class ExhibitionRes {

        @Schema(type = "Long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "OperatingKeyword", example = "ON_DISPLAY", description = "전시회 운영 키워드를 출력합니다.")
        private OperatingKeyword operatingKeyword;

        @Schema(type = "PriceKeyword", example = "FREE", description = "전시회 가격 키워드를 출력합니다.")
        private PriceKeyword priceKeyword;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;

        private PlaceResponseDto.PlaceInfoRes place;
    }

    // 전시회 상세 정보 조회
    @Data
    @Builder
    public static class ExhibitionDetailRes {

        // Description : Exhibition
        @Schema(type = "Long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "String", example = "20231122", description = "전시회 시작 날짜를 출력합니다.")
        private String startDate;

        @Schema(type = "String", example = "20231127", description = "전시회 종료 날짜를 출력합니다.")
        private String endDate;

        @Schema(type = "OperatingKeyword", example = "ON_DISPLAY", description = "전시회 운영 키워드를 출력합니다.")
        private OperatingKeyword operatingKeyword;

        @Schema(type = "PriceKeyword", example = "FREE", description = "전시회 가격 키워드를 출력합니다.")
        private PriceKeyword priceKeyword;

        @Schema(type = "String", example = "전시회정보전시회정보", description = "전시회 정보를 출력합니다.")
        private String information;

        @Schema(type = "Double", example = "3.5", description = "전시회 별점을 출력합니다.")
        private Double rate;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;

        // Description : Check isInterest
        private Boolean checkInterest;

        // Description : My Rating
        private Double myRating;

        // Description : Place
        private PlaceResponseDto.PlaceRes place;
    }

    // 랜덤 전시회 2개 조회
    @Data
    @Builder
    public static class RandomExhibitionRes {

        @Schema(type = "Long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;

    }

    @Data
    @Builder
    public static class PosterRes {

        @Schema(type = "Long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;
    }

    @Data
    @Builder
    public static class ExhibitionInPlaceRes {

        @Schema(type = "Long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "OperatingKeyword", example = "ON_DISPLAY", description = "전시회 운영 키워드를 출력합니다.")
        private OperatingKeyword operatingKeyword;

        @Schema(type = "PriceKeyword", example = "FREE", description = "전시회 가격 키워드를 출력합니다.")
        private PriceKeyword priceKeyword;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;

        @Schema(type = "String", example = "20231122", description = "전시회 시작 날짜를 출력합니다.")
        private String startDate;

        @Schema(type = "String", example = "20231127", description = "전시회 종료 날짜를 출력합니다.")
        private String endDate;
    }

    @Data
    @Builder
    public static class SearchExhibitionByNameRes {

        @Schema(type = "long", example = "1", description = "전시회 ID를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "String", example = "http://www.culture.go.kr/upload/rdf/23/11/rdf_2023112721202875517.jpg", description = "전시회 이미지 포스터를 출력합니다.")
        private String imageUrl;
    }
}
