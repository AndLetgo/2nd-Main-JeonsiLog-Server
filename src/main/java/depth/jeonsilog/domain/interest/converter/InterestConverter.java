package depth.jeonsilog.domain.interest.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.dto.InterestResponseDto;
import depth.jeonsilog.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class InterestConverter {

    public static Interest toInterest(User user, Exhibition exhibition) {
        return Interest.builder()
                .user(user)
                .exhibition(exhibition)
                .build();
    }

    public static InterestResponseDto.InterestRes toInterestRes(Interest interest) {
        return InterestResponseDto.InterestRes.builder()
                .interestId(interest.getId())
                .userId(interest.getUser().getId())
                .exhibitionId(interest.getExhibition().getId())
                .build();
    }

    public static List<InterestResponseDto.InterestListRes> toInterestListRes(List<Interest> interest) {
        List<InterestResponseDto.InterestListRes> interestListRes = new ArrayList<>();

        for (Interest interests : interest) {
            Exhibition exhibition = interests.getExhibition();
            InterestResponseDto.InterestListRes interestRes = InterestResponseDto.InterestListRes.builder()
                    .interestId(interests.getId())
                    .userId(interests.getUser().getId())
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .imageUrl(exhibition.getImageUrl())
                    .placeName(exhibition.getPlace().getName())
                    .operatingKeyword(exhibition.getOperatingKeyword())
                    .priceKeyword(exhibition.getPriceKeyword())
                    .build();

            interestListRes.add(interestRes);
        }
        return interestListRes;
    }
}
