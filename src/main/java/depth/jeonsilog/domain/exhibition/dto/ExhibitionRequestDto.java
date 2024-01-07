package depth.jeonsilog.domain.exhibition.dto;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.place.dto.PlaceRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

public class ExhibitionRequestDto {

    @Data
    public static class UpdateExhibitionDetailReq {

        @Schema(type = "Long", example = "1", description = "전시회 ID입니다.")
        private Long exhibitionId;

        @Schema(type = "String", example = "한국필묵그룹 선흔 창립20주년 기념전", description = "전시회 이름입니다.")
        private String exhibitionName;

        @Schema(type = "OperatingKeyword", example = "ON_DISPLAY", description = "전시회 운영 키워드입니다.")
        private OperatingKeyword operatingKeyword;

        @Schema(type = "PriceKeyword", example = "FREE", description = "전시회 가격 키워드입니다.")
        private PriceKeyword priceKeyword;

        @Schema(type = "String", example = "이 전시회는 어떤 전시회입니다.", description = "전시회 정보입니다.")
        @Size(max = 1000, message = "1000자 이하로만 작성 가능합니다.")
        private String information;

        @Schema(type = "Boolean", example = "false", description = "전시회 포스터 이미지 변동 여부 입니다.")
        private Boolean isImageChange;

        private PlaceRequestDto.UpdatePlaceWithExhibitionDetailReq updatePlaceInfo;

    }

    @Data
    public static class UpdateExhibitionSequence {

        @Schema(type = "Integer", example = "1", description = "설정할 전시회 순서입니다.")
        private Integer sequence;

        @Schema(type = "Long", example = "1", description = "전시회 ID입니다.")
        private Long exhibitionId;
    }

    @Data
    public static class UpdateExhibitionSequenceList {

        private List<UpdateExhibitionSequence> updateSequenceInfo;
    }
}
