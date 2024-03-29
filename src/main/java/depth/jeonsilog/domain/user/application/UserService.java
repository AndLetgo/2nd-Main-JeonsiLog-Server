package depth.jeonsilog.domain.user.application;

import depth.jeonsilog.domain.auth.domain.Token;
import depth.jeonsilog.domain.auth.domain.repository.TokenRepository;
import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.repository.InterestRepository;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.reply.domain.repository.ReplyRepository;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.domain.repository.ReviewRepository;
import depth.jeonsilog.domain.s3.application.S3Uploader;
import depth.jeonsilog.domain.user.converter.UserConverter;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.domain.user.dto.UserRequestDto;
import depth.jeonsilog.domain.user.dto.UserResponseDto;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final TokenRepository tokenRepository;

    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final RatingRepository ratingRepository;
    private final InterestRepository interestRepository;

    private final S3Uploader s3Uploader;

    private static final String DIRNAME = "profile_img";

    // 토큰으로 본인 정보 조회
    public ResponseEntity<?> findUserByToken(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(userRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 id로 정보 조회
    public ResponseEntity<?> findUserById(UserPrincipal userPrincipal, Long userId) {

        validateUserByToken(userPrincipal);

        User findUser = validateUserById(userId);
        UserResponseDto.UserRes userRes = UserConverter.toUserRes(findUser, followRepository);

        ApiResponse apiResponse = ApiResponse.toApiResponse(userRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 닉네임 변경
    @Transactional
    public ResponseEntity<?> changeNickname(UserPrincipal userPrincipal, UserRequestDto.ChangeNicknameReq changeNicknameReq) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateNickname(changeNicknameReq.getNickname());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("닉네임을 변경했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 프로필 변경
    @Transactional
    public ResponseEntity<?> changeProfile(UserPrincipal userPrincipal, MultipartFile img) throws IOException {
        User findUser = validateUserByToken(userPrincipal);

        String storedFileName = null;
        if (img != null) {
            storedFileName = s3Uploader.upload(img, DIRNAME);
        }
        findUser.updateProfileImg(storedFileName);

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("프로필 사진을 변경했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 검색
    public ResponseEntity<?> searchUsers(Integer page, UserPrincipal userPrincipal, String searchWord) {

        validateUserByToken(userPrincipal);

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        Slice<User> userPage = userRepository.findSliceByNicknameContaining(pageRequest, searchWord);
        List<User> userList = userPage.getContent();

        DefaultAssert.isTrue(!userList.isEmpty(), "해당 검색어를 포함한 유저가 존재하지 않습니다.");

        List<UserResponseDto.SearchUsersRes> searchUsersResList = UserConverter.toSearchUsersRes(userList);
        boolean hasNextPage = userPage.hasNext();
        UserResponseDto.SearchUserWithPaging searchUserWithPaging = UserConverter.toSearchUserWithPaging(hasNextPage, searchUsersResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(searchUserWithPaging);

        return ResponseEntity.ok(apiResponse);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<?> deleteUser(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);
        Long userId = findUser.getId();

        // 즐겨찾기, 별점, 감상평, 댓글 DELETE 처리
        List<Review> reviews = reviewRepository.findAllReviewsByUserId(userId);
        List<Rating> ratings = ratingRepository.findAllByUserId(userId);
        List<Interest> interests = interestRepository.findAllByUserId(userId);

        // review의 replies도
        for (Review review : reviews) {
            List<Reply> replyByReview = replyRepository.findAllRepliesByReviewId(review.getId());
            review.updateNumReply(review.getNumReply() - replyByReview.size());
            replyRepository.deleteAll(replyByReview);
        }

        // 얘만 여기 있는 이유 : 바로 위에서 지운 reply와 겹치지 않도록 하기 위함
        List<Reply> replyByUser = replyRepository.findAllRepliesByUserId(userId);
        for (Reply reply : replyByUser) {
            Review review = reply.getReview();
            if (reviewRepository.findReviewByReviewId(review.getId()) != null)
                review.updateNumReply(review.getNumReply() - 1);
        }
        replyRepository.deleteAll(replyByUser);

        // soft delete를 통한 별점 변동 x
        for (Rating rating : ratings) {
            rating.updateStatus(Status.DELETE);
            rating.deleteUser();
        }

        reviewRepository.deleteAll(reviews);
//        reviewRepository.deleteAllInBatch(reviews);
        interestRepository.deleteAll(interests);

        // s3에서 프로필 이미지 삭제
        s3Uploader.deleteImage(DIRNAME, findUser.getProfileImg());
        userRepository.delete(findUser); // hard delete

        Optional<Token> token = tokenRepository.findByUserEmail(userPrincipal.getEmail());
        DefaultAssert.isTrue(token.isPresent(), "이미 탈퇴되었습니다.");
        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("회원이 탈퇴되었습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 포토 캘린더 공개/비공개 변경
    @Transactional
    public ResponseEntity<?> switchIsOpen(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsOpen(!findUser.getIsOpen());

        UserResponseDto.SwitchIsOpenRes switchIsOpenRes = UserConverter.toSwitchIsOpenRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsOpenRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 팔로잉 알림 수신 여부 변경
    @Transactional
    public ResponseEntity<?> switchIsRecvExhibition(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsRecvExhibition(!findUser.getIsRecvExhibition());

        UserResponseDto.SwitchIsRecvExhibitionRes switchIsRecvExhbitionRes = UserConverter.toSwitchIsRecvExhibitionRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsRecvExhbitionRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 나의 활동 알림 수신 여부 변경
    @Transactional
    public ResponseEntity<?> switchIsRecvActive(UserPrincipal userPrincipal) {

        User findUser = validateUserByToken(userPrincipal);

        findUser.updateIsRecvActive(!findUser.getIsRecvActive());

        UserResponseDto.SwitchIsRecvActiveRes switchIsRecvActiveRes = UserConverter.toSwitchIsRecvActive(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(switchIsRecvActiveRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 유저 포토캘린더 공개 여부 조회
    public ResponseEntity<?> getIsOpen(Long userId) {
        User findUser = validateUserById(userId);
        UserResponseDto.IsOpenRes responseDto = UserConverter.toIsOpenRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(responseDto);
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 활동 및 전시 알림 수신 여부 조회
    public ResponseEntity<?> getRecvOrNot(UserPrincipal userPrincipal) {
        User findUser = validateUserByToken(userPrincipal);
        UserResponseDto.IsRecvOrNotRes isRecvOrNotRes = UserConverter.toIsRecvOrNotRes(findUser);

        ApiResponse apiResponse = ApiResponse.toApiResponse(isRecvOrNotRes);
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> updateFcmToken(UserPrincipal userPrincipal, UserRequestDto.UpdateFcmToken updateFcmToken) {
        User findUser = validateUserByToken(userPrincipal);
        findUser.updateFcmToken(updateFcmToken.getFcmToken());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("Fcm Token이 업데이트 되었습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    public User validateUserByToken(UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

    public User validateUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "유저 정보가 올바르지 않습니다.");
        return user.get();
    }

}
