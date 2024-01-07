package depth.jeonsilog.domain.reply.application;

import depth.jeonsilog.domain.alarm.application.AlarmService;
import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.reply.converter.ReplyConverter;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.reply.domain.repository.ReplyRepository;
import depth.jeonsilog.domain.reply.dto.ReplyRequestDto;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;
import depth.jeonsilog.domain.review.application.ReviewService;
import depth.jeonsilog.domain.review.domain.Review;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final AlarmService alarmService;

    // Description : 댓글 목록 조회
    public ResponseEntity<?> findReplyList(Integer page, Long reviewId) {

        Review review = reviewService.validateReviewById(reviewId);

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
        Page<Reply> replyPage = replyRepository.findByReview(pageRequest, review);
        List<Reply> replies = replyPage.getContent();

        DefaultAssert.isTrue(!replies.isEmpty(), "해당 감상평에 대한 댓글이 존재하지 않습니다.");

        List<ReplyResponseDto.ReplyRes> replyResList = ReplyConverter.toReplyResList(replies);

        ApiResponse apiResponse = ApiResponse.toApiResponse(replyResList);
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 댓글 작성
    @Transactional
    public ResponseEntity<?> createReply(UserPrincipal userPrincipal, ReplyRequestDto.CreateReplyReq createReplyReq) {

        Review review = reviewService.validateReviewById(createReplyReq.getReviewId());
        User user = userService.validateUserByToken(userPrincipal);

        Reply reply = ReplyConverter.toReply(review, user, createReplyReq);
        replyRepository.save(reply);

        review.updateNumReply(review.getNumReply() + 1);

        alarmService.makeReplyAlarm(reply);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("댓글을 작성했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteReply(UserPrincipal userPrincipal, Long replyId) {

        User user = userService.validateUserByToken(userPrincipal);
        Reply reply = validateReplyById(replyId);
        Review review = reply.getReview();

        DefaultAssert.isTrue(user.equals(reply.getUser()) || user.getRole().equals(Role.ADMIN)
                , "해당 댓글의 작성자 혹은 관리자만 삭제할 수 있습니다.");
        reply.updateStatus(Status.DELETE);

        review.updateNumReply(review.getNumReply() - 1);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("댓글을 삭제했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    public Reply validateReplyById(Long replyId) {
        Optional<Reply> reply = replyRepository.findById(replyId);
        DefaultAssert.isTrue(reply.isPresent(), "댓글 정보가 올바르지 않습니다.");
        return reply.get();
    }
}
