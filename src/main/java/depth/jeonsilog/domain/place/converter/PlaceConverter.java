package depth.jeonsilog.domain.place.converter;

import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.dto.PlaceResponseDto;

import java.util.ArrayList;
import java.util.List;

public class PlaceConverter {

    // PLACE -> PlaceRes
    public static PlaceResponseDto.PlaceRes toPlaceRes(Place place) {
        return PlaceResponseDto.PlaceRes.builder()
                .placeId(place.getId())
                .placeName(place.getName())
                .address(place.getAddress())
                .tel(place.getTel())
                .homePage(place.getHomePage())
                .build();
    }

    // Places -> PlaceInfoListRes
    public static List<PlaceResponseDto.PlaceInfoRes> toPlaceInfoListRes(List<Place> places) {

        List<PlaceResponseDto.PlaceInfoRes> placeInfoResList = new ArrayList<>();

        for (Place place : places) {
            PlaceResponseDto.PlaceInfoRes placeInfoRes = PlaceResponseDto.PlaceInfoRes.builder()
                    .placeId(place.getId())
                    .placeName(place.getName())
                    .placeAddress(place.getAddress())
                    .build();

            placeInfoResList.add(placeInfoRes);
        }

        return placeInfoResList;
    }

    public static PlaceResponseDto.PlaceInfoResWithPaging toPlaceInfoResWithPaging(boolean hasNextPage, List<PlaceResponseDto.PlaceInfoRes> placeInfoResList) {
        return PlaceResponseDto.PlaceInfoResWithPaging.builder()
                .hasNextPage(hasNextPage)
                .data(placeInfoResList)
                .build();
    }

}
