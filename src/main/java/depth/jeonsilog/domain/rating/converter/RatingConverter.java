package depth.jeonsilog.domain.rating.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.dto.RatingResponseDto;
import depth.jeonsilog.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class RatingConverter {

    public static Rating toRating(User user, Exhibition exhibition, Double rate) {
        return Rating.builder()
                .user(user)
                .exhibition(exhibition)
                .rate(rate)
                .build();
    }

    public static List<RatingResponseDto.RatingRes> toRatingRes(List<Rating> ratingList) {
        List<RatingResponseDto.RatingRes> ratingResList = new ArrayList<>();
        for (Rating rating : ratingList) {
            Exhibition exhibition = rating.getExhibition();
            RatingResponseDto.RatingRes ratingRes = RatingResponseDto.RatingRes.builder()
                    .ratingId(rating.getId())
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .rate(rating.getRate())
                    .build();

            ratingResList.add(ratingRes);
        }
        return ratingResList;
    }

    public static RatingResponseDto.RatingListRes toRatingListRes(Integer num, boolean hasNextPage, List<RatingResponseDto.RatingRes> ratingRes) {
        return RatingResponseDto.RatingListRes.builder()
                .numRating(num)
                .hasNextPage(hasNextPage)
                .data(ratingRes)
                .build();
    }
}
