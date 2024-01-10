package depth.jeonsilog.domain.interest.dto;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class InterestResponseDto {

    @Data
    @Builder
    public static class InterestRes {

        @Schema(type = "long", example = "1", description = "즐겨찾기의 id를 출력합니다.")
        private Long interestId;

        @Schema(type = "long", example = "2", description = "유저의 id를 출력합니다.")
        private Long userId;

        @Schema(type = "long", example = "3", description = "전시회 id를 출력합니다.")
        private Long exhibitionId;
    }

    @Data
    @Builder
    public static class InterestListRes {
        @Schema(type = "long", example = "1", description = "즐겨찾기의 id를 출력합니다.")
        private Long interestId;

        @Schema(type = "long", example = "2", description = "유저의 id를 출력합니다.")
        private Long userId;

        @Schema(type = "long", example = "3", description = "전시회 id를 출력합니다.")
        private Long exhibitionId;

        @Schema(type = "string", example = "안드레고 개인전", description = "전시회 이름을 출력합니다.")
        private String exhibitionName;

        @Schema(type = "string", example = "갤러리 뎁스", description = "전시공간 이름을 출력합니다.")
        private String placeName;

        @Schema(type = "string", example = "http://..", description = "전시회 포스터 url을 출력합니다.")
        private String imageUrl;

        @Schema(type = "string", example = "전시중", description = "전시회 운영 정보를 출력합니다.")
        private OperatingKeyword operatingKeyword;

        @Schema(type = "string", example = "무료", description = "전시회 가격 정보를 출력합니다.")
        private PriceKeyword priceKeyword;
    }

    @Data
    @Builder
    public static class InterestListResWithPaging {
        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<InterestResponseDto.InterestListRes> data;
    }
}
