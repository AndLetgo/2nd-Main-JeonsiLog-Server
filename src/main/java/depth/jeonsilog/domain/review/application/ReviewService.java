package depth.jeonsilog.domain.review.application;


import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.review.converter.ReviewConverter;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.domain.repository.ReviewRepository;
import depth.jeonsilog.domain.review.dto.ReviewRequestDto;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final RatingRepository ratingRepository;
    private final UserService userService;

    // 감상평 작성
    @Transactional
    public ResponseEntity<?> writeReview(UserPrincipal userPrincipal, ReviewRequestDto.WriteReviewReq writeReviewReq) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Exhibition> exhibition = exhibitionRepository.findById(writeReviewReq.getExhibitionId());
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");

        Review review = ReviewConverter.toReview(findUser, exhibition.get(), writeReviewReq.getContents());
        reviewRepository.save(review);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("감상평을 작성했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 감상평 삭제
    @Transactional
    public ResponseEntity<?> deleteReview(UserPrincipal userPrincipal, Long reviewId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Review> review = reviewRepository.findById(reviewId);
        DefaultAssert.isTrue(review.isPresent(), "Review id가 올바르지 않습니다.");
        Review findReview = review.get();

        DefaultAssert.isTrue(findUser.equals(findReview.getUser()), "해당 리뷰의 작성자만 삭제할 수 있습니다.");
        findReview.updateStatus(Status.DELETE);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("감상평을 삭제했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 전시회의 감상평 목록 조회
    public ResponseEntity<?> getReviewList(Long exhibitionId) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");

        List<Review> reviewList = reviewRepository.findAllByExhibitionId(exhibitionId);
        List<ReviewResponseDto.ReviewListRes> reviewListRes = ReviewConverter.toReviewListRes(reviewList, ratingRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 나의 감상평 목록 조회
    public ResponseEntity<?> getMyReviewList(UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<Review> reviewList = reviewRepository.findAllByUserId(findUser.getId());

        List<ReviewResponseDto.UserReviewRes> reviewRes = ReviewConverter.toUserReviewRes(reviewList);
        Integer numReview = reviewList.size();
        ReviewResponseDto.UserReviewListRes reviewListRes = ReviewConverter.toUserReviewListRes(numReview, reviewRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 타 유저의 감상평 목록 조회
    public ResponseEntity<?> getUserReviewList(Long userId) {
        User findUser = userService.validateUserById(userId);
        List<Review> reviewList = reviewRepository.findAllByUserId(findUser.getId());

        List<ReviewResponseDto.UserReviewRes> reviewRes = ReviewConverter.toUserReviewRes(reviewList);
        Integer numReview = reviewList.size();
        ReviewResponseDto.UserReviewListRes reviewListRes = ReviewConverter.toUserReviewListRes(numReview, reviewRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }
}