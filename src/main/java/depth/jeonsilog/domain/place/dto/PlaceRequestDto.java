package depth.jeonsilog.domain.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class PlaceRequestDto {

    @Data
    public static class UpdatePlaceWithExhibitionDetailReq {

        @Schema(type = "Long", example = "1", description = "전시 공간 ID입니다.")
        private Long placeId;

        @Schema(type = "String", example = "예술의전당(한가람미술관)", description = "전시 공간 이름입니다.")
        private String placeName;

        @Schema(type = "String", example = "서울특별시 송파구 올림픽로 300 롯데월드몰 8층 롯데콘서트홀", description = "전시 공간 주소입니다.")
        private String placeAddress;

        @Schema(type = "String", example = "031-590-4358", description = "전시 공간 전화번호입니다.")
        private String placeTel;

        @Schema(type = "String", example = "\thttps://culture.nyj.go.kr/home/", description = "전시 공간 홈페이지입니다.")
        private String placeHomePage;

    }
}
