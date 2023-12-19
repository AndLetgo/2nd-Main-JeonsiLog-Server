package depth.jeonsilog.domain.exhibition.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.global.DefaultAssert;

import java.util.ArrayList;
import java.util.List;

public class ExhibitionConverter {

    // EXHIBITION & PlaceRes & ImageRes -> ExhibitionRes
    public static ExhibitionResponseDto.ExhibitionRes toExhibitionRes(Exhibition exhibition, PlaceResponseDto.PlaceRes placeRes) {
        return ExhibitionResponseDto.ExhibitionRes.builder()
                .exhibitionId(exhibition.getId())
                .exhibitionName(exhibition.getName())
                .startDate(exhibition.getStartDate())
                .endDate(exhibition.getEndDate())
                .operatingKeyword(exhibition.getOperatingKeyword())
                .priceKeyword(exhibition.getPriceKeyword())
                .information(exhibition.getInformation())
                .rate(exhibition.getRate())
                .sequence(exhibition.getSequence())
                .imageUrl(exhibition.getImageUrl())
                .place(placeRes)
                .build();
    }

    // EXHIBITION & ImageRes -> RandomExhibitionRes
    public static ExhibitionResponseDto.RandomExhibitionRes toRandomExhibitionRes(Exhibition randomExhibition) {
        return ExhibitionResponseDto.RandomExhibitionRes.builder()
                .exhibitionId(randomExhibition.getId())
                .exhibitionName(randomExhibition.getName())
                .imageUrl(randomExhibition.getImageUrl())
                .build();
    }

    // Exhibitions -> SearchExhibitionListRes
    public static List<ExhibitionResponseDto.SearchExhibitionRes> toSearchExhibitionListRes(List<Exhibition> exhibitions, List<PlaceResponseDto.PlaceInfoRes> placeInfoListRes) {

        DefaultAssert.isTrue(exhibitions.size() == placeInfoListRes.size(), "올바르지 않은 정보입니다.");

        List<ExhibitionResponseDto.SearchExhibitionRes> searchExhibitionResList = new ArrayList<>();
        for (int i = 0; i < exhibitions.size(); i++) {
            Exhibition exhibition = exhibitions.get(i);
            PlaceResponseDto.PlaceInfoRes placeInfoRes = placeInfoListRes.get(i);

            ExhibitionResponseDto.SearchExhibitionRes searchExhibitionRes = ExhibitionResponseDto.SearchExhibitionRes.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .operatingKeyword(exhibition.getOperatingKeyword())
                    .priceKeyword(exhibition.getPriceKeyword())
                    .place(placeInfoRes)
                    .build();

            searchExhibitionResList.add(searchExhibitionRes);
        }

        return searchExhibitionResList;
    }
}
