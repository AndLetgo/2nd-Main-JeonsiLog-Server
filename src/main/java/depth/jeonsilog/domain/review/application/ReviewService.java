package depth.jeonsilog.domain.review.application;


import depth.jeonsilog.domain.alarm.application.AlarmService;
import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.reply.domain.repository.ReplyRepository;
import depth.jeonsilog.domain.review.converter.ReviewConverter;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.domain.repository.ReviewRepository;
import depth.jeonsilog.domain.review.dto.ReviewRequestDto;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final ReplyRepository replyRepository;

    private final UserService userService;
    private final AlarmService alarmService;

    // 감상평 작성
    @Transactional
    public ResponseEntity<?> writeReview(UserPrincipal userPrincipal, ReviewRequestDto.WriteReviewReq writeReviewReq) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Exhibition> exhibition = exhibitionRepository.findById(writeReviewReq.getExhibitionId());
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");

        Review review = ReviewConverter.toReview(findUser, exhibition.get(), writeReviewReq.getContents());
        reviewRepository.save(review);

        alarmService.makeReviewAlarm(review);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("감상평을 작성했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 감상평 삭제
    @Transactional
    public ResponseEntity<?> deleteReview(UserPrincipal userPrincipal, Long reviewId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Review findReview = validateReviewById(reviewId);

        DefaultAssert.isTrue(findUser.equals(findReview.getUser()) || findUser.getRole().equals(Role.ADMIN)
                , "해당 리뷰의 작성자 혹은 관리자만 삭제할 수 있습니다.");

        List<Reply> replyList = replyRepository.findByReview(findReview);
        for (Reply reply : replyList) {
            reply.updateStatus(Status.DELETE);
        }

        findReview.updateStatus(Status.DELETE);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("감상평을 삭제했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 전시회의 감상평 목록 조회
    public ResponseEntity<?> getReviewList(Integer page, Long exhibitionId) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");

        PageRequest pageRequest = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Review> reviewPage = reviewRepository.findByExhibitionId(pageRequest, exhibitionId);
        List<Review> reviewList = reviewPage.getContent();

        DefaultAssert.isTrue(!reviewList.isEmpty(), "해당 전시회에 대한 감상평이 존재하지 않습니다.");

        List<ReviewResponseDto.ReviewListRes> reviewListRes = ReviewConverter.toReviewListRes(reviewList, ratingRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 나의 감상평 목록 조회
    public ResponseEntity<?> getMyReviewList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);

        PageRequest pageRequest = PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Review> reviewPage = reviewRepository.findByUserId(pageRequest, findUser.getId());
        List<Review> reviewList = reviewPage.getContent();

        DefaultAssert.isTrue(!reviewList.isEmpty(), "해당 유저가 작성한 감상평이 존재하지 않습니다.");

        List<ReviewResponseDto.UserReviewRes> reviewRes = ReviewConverter.toUserReviewRes(reviewList);

        Long totalElements = reviewPage.getTotalElements();
        Integer numReview = totalElements.intValue();

        ReviewResponseDto.UserReviewListRes reviewListRes = ReviewConverter.toUserReviewListRes(numReview, reviewRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 타 유저의 감상평 목록 조회
    public ResponseEntity<?> getUserReviewList(Integer page, Long userId) {
        User findUser = userService.validateUserById(userId);

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Review> reviewPage = reviewRepository.findByUserId(pageRequest, findUser.getId());
        List<Review> reviewList = reviewPage.getContent();

        DefaultAssert.isTrue(!reviewList.isEmpty(), "해당 유저가 작성한 감상평이 존재하지 않습니다.");

        List<ReviewResponseDto.UserReviewRes> reviewRes = ReviewConverter.toUserReviewRes(reviewList);

        Long totalElements = reviewPage.getTotalElements();
        Integer numReview = totalElements.intValue();

        ReviewResponseDto.UserReviewListRes reviewListRes = ReviewConverter.toUserReviewListRes(numReview, reviewRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reviewListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 전시회에 감상평을 남겼는지 체크
    public ResponseEntity<?> checkIsWrite(UserPrincipal userPrincipal, Long exhibitionId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Exhibition> findExhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(findExhibition.isPresent(), "전시회 정보가 올바르지 않습니다.");

        Boolean isWrite = reviewRepository.existsByUserIdAndExhibitionId(findUser.getId(), exhibitionId);
        ReviewResponseDto.CheckIsWriteRes responseDto = ReviewResponseDto.CheckIsWriteRes.builder().isWrite(isWrite).build();
        ApiResponse apiResponse = ApiResponse.toApiResponse(responseDto);
        return ResponseEntity.ok(apiResponse);
    }

    public Review validateReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        DefaultAssert.isTrue(review.isPresent(), "감상평 정보가 올바르지 않습니다.");
        return review.get();
    }
}
