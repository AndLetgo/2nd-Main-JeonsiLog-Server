package depth.jeonsilog.domain.review.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewConverter {

    public static Review toReview(User user, Exhibition exhibition, String contents) {
        return Review.builder()
                .user(user)
                .exhibition(exhibition)
                .contents(contents)
                .build();
    }

    public static List<ReviewResponseDto.ReviewListRes> toReviewListRes(List<Review> reviews, RatingRepository ratingRepository) {
        List<ReviewResponseDto.ReviewListRes> reviewListRes = new ArrayList<>();
        for (Review review : reviews) {
            User user = review.getUser();
            Exhibition exhibition = review.getExhibition();
            Optional<Rating> rating = ratingRepository.findByUserIdAndExhibitionId(user.getId(), exhibition.getId());
            ReviewResponseDto.ReviewListRes reviewRes = ReviewResponseDto.ReviewListRes.builder()
                    .reviewId(review.getId())
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .imgUrl(user.getProfileImg())
                    .rate(rating.isPresent() ? rating.get().getRate() : 0)
                    .contents(review.getContents())
                    .numReply(review.getNumReply())
                    .build();
            reviewListRes.add(reviewRes);
        }
        return reviewListRes;
    }

    public static List<ReviewResponseDto.UserReviewRes> toUserReviewRes(List<Review> reviews) {
        List<ReviewResponseDto.UserReviewRes> reviewResList = new ArrayList<>();
        for (Review review : reviews) {
            Exhibition exhibition = review.getExhibition();
            ReviewResponseDto.UserReviewRes reviewRes = ReviewResponseDto.UserReviewRes.builder()
                    .reviewId(review.getId())
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getName())
                    .exhibitionImgUrl(exhibition.getImageUrl())
                    .contents(review.getContents())
                    .build();

            reviewResList.add(reviewRes);
        }
        return reviewResList;
    }

    public static ReviewResponseDto.UserReviewListRes toUserReviewListRes(Integer numReview, List<ReviewResponseDto.UserReviewRes> reviewRes) {
        return ReviewResponseDto.UserReviewListRes.builder()
                .numReview(numReview)
                .data(reviewRes)
                .build();
    }
}
