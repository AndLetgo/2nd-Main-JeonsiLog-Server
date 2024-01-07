package depth.jeonsilog.domain.exhibition.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;
import depth.jeonsilog.global.DefaultAssert;

import java.util.ArrayList;
import java.util.List;

public class ExhibitionConverter {

    // Exhibitions & placeInfoResList -> exhibitionResList
    public static List<ExhibitionResponseDto.ExhibitionRes> toExhibitionListRes(List<Exhibition> exhibitions, List<PlaceResponseDto.PlaceInfoRes> placeInfoListRes) {

        DefaultAssert.isTrue(exhibitions.size() == placeInfoListRes.size(), "올바르지 않은 정보입니다.");

        List<ExhibitionResponseDto.ExhibitionRes> exhibitionResList = new ArrayList<>();
        for (int i = 0; i < exhibitions.size(); i++) {
            Exhibition exhibition = exhibitions.get(i);
            PlaceResponseDto.PlaceInfoRes placeInfoRes = placeInfoListRes.get(i);

            ExhibitionResponseDto.ExhibitionRes exhibitionRes = ExhibitionResponseDto.ExhibitionRes.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .operatingKeyword(exhibition.getOperatingKeyword())
                    .priceKeyword(exhibition.getPriceKeyword())
                    .imageUrl(exhibition.getImageUrl())
                    .place(placeInfoRes)
                    .build();

            exhibitionResList.add(exhibitionRes);
        }

        return exhibitionResList;
    }

    // EXHIBITION & PlaceRes -> ExhibitionDetailRes
    public static ExhibitionResponseDto.ExhibitionDetailRes toExhibitionDetailRes(Exhibition exhibition, PlaceResponseDto.PlaceRes placeRes, Boolean checkInterest, Double myRate) {
        return ExhibitionResponseDto.ExhibitionDetailRes.builder()
                .exhibitionId(exhibition.getId())
                .exhibitionName(exhibition.getName())
                .startDate(exhibition.getStartDate())
                .endDate(exhibition.getEndDate())
                .operatingKeyword(exhibition.getOperatingKeyword())
                .priceKeyword(exhibition.getPriceKeyword())
                .information(exhibition.getInformation())
                .rate(exhibition.getRate())
                .imageUrl(exhibition.getImageUrl())
                .place(placeRes)
                .checkInterest(checkInterest)
                .myRating(myRate)
                .build();
    }

    // EXHIBITION -> RandomExhibitionRes
    public static ExhibitionResponseDto.RandomExhibitionRes toRandomExhibitionRes(Exhibition randomExhibition) {
        return ExhibitionResponseDto.RandomExhibitionRes.builder()
                .exhibitionId(randomExhibition.getId())
                .exhibitionName(randomExhibition.getName())
                .imageUrl(randomExhibition.getImageUrl())
                .build();
    }

    // EXHIBITION -> PosterRes
    public static ExhibitionResponseDto.PosterRes toPosterRes(Exhibition exhibition) {
        return ExhibitionResponseDto.PosterRes.builder()
                .exhibitionId(exhibition.getId())
                .imageUrl(exhibition.getImageUrl())
                .build();
    }

    // Exhibitions -> ExhibitionListInPlaceResList
    public static List<ExhibitionResponseDto.ExhibitionInPlaceRes> toExhibitionListInPlaceRes(List<Exhibition> exhibitions) {

        List<ExhibitionResponseDto.ExhibitionInPlaceRes> exhibitionInPlaceResList = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            ExhibitionResponseDto.ExhibitionInPlaceRes exhibitionInPlaceRes = ExhibitionResponseDto.ExhibitionInPlaceRes.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .operatingKeyword(exhibition.getOperatingKeyword())
                    .priceKeyword(exhibition.getPriceKeyword())
                    .imageUrl(exhibition.getImageUrl())
                    .build();

            exhibitionInPlaceResList.add(exhibitionInPlaceRes);
        }

        return exhibitionInPlaceResList;
    }

    public static List<ExhibitionResponseDto.SearchExhibitionByNameRes> toSearchByNameRes(List<Exhibition> exhibitions) {

        List<ExhibitionResponseDto.SearchExhibitionByNameRes> exhibitionByNameResList = new ArrayList<>();

        for (Exhibition exhibition : exhibitions) {
            ExhibitionResponseDto.SearchExhibitionByNameRes searchExhibitionByNameRes = ExhibitionResponseDto.SearchExhibitionByNameRes.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .imageUrl(exhibition.getImageUrl())
                    .build();

            exhibitionByNameResList.add(searchExhibitionByNameRes);
        }
        return exhibitionByNameResList;
    }
}
