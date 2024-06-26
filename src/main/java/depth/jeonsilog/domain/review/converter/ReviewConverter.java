package depth.jeonsilog.domain.review.converter;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.dto.UserResponseDto;

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
                    .createdDate(review.getCreatedDate())
                    .userLevel(user.getUserLevel())
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
                    .createdDate(review.getCreatedDate())
                    .build();

            reviewResList.add(reviewRes);
        }
        return reviewResList;
    }

    public static ReviewResponseDto.UserReviewListRes toUserReviewListRes(Integer numReview, boolean hasNextPage, List<ReviewResponseDto.UserReviewRes> reviewRes) {
        return ReviewResponseDto.UserReviewListRes.builder()
                .numReview(numReview)
                .hasNextPage(hasNextPage)
                .data(reviewRes)
                .build();
    }

    public static ReviewResponseDto.ReviewListResList toReviewListResList(boolean hasNextPage, List<ReviewResponseDto.ReviewListRes> reviewListRes) {
        return ReviewResponseDto.ReviewListResList.builder()
                .hasNextPage(hasNextPage)
                .data(reviewListRes)
                .build();
    }

    public static ReviewResponseDto.ReviewListRes toReviewListRes(Review review, UserResponseDto.SearchUsersRes userRes, Double rate) {
        return ReviewResponseDto.ReviewListRes.builder()
                .reviewId(review.getId())
                .exhibitionId(review.getExhibition().getId())
                .userId(userRes.getUserId())
                .imgUrl(userRes.getProfileImgUrl())
                .nickname(userRes.getNickname())
                .contents(review.getContents())
                .rate(rate)
                .numReply(review.getNumReply())
                .createdDate(review.getCreatedDate())
                .userLevel(userRes.getUserLevel())
                .build();
    }

    public static ReviewResponseDto.CheckIsWriteRes toCheckIsWriteRes(Optional<Review> findReview) {
        Boolean isWrite = false;
        Long reviewId = null;
        String contents = "";
        if (findReview.isPresent()) {
            Review review = findReview.get();
            isWrite = true;
            reviewId = review.getId();
            contents = review.getContents();
        } else {
            contents = null;
        }

        return ReviewResponseDto.CheckIsWriteRes.builder()
                .isWrite(isWrite)
                .reviewId(reviewId)
                .contents(contents)
                .build();
    }
}
