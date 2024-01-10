package depth.jeonsilog.domain.rating.application;

import depth.jeonsilog.domain.alarm.application.AlarmService;
import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.rating.converter.RatingConverter;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.rating.domain.repository.RatingRepository;
import depth.jeonsilog.domain.rating.dto.RatingRequestDto;
import depth.jeonsilog.domain.rating.dto.RatingResponseDto;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.application.UserService;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    private final ExhibitionService exhibitionService;
    private final UserService userService;
    private final AlarmService alarmService;

    // 별점 등록
    @Transactional
    public ResponseEntity<?> registerRating(UserPrincipal userPrincipal, RatingRequestDto.RatingReq ratingReq) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Exhibition exhibition = exhibitionService.validateExhibitionById(ratingReq.getExhibitionId());

        Optional<Rating> checkDuplication = ratingRepository.findByUserIdAndExhibitionId(findUser.getId(), exhibition.getId());
        DefaultAssert.isTrue(checkDuplication.isEmpty(), "해당하는 별점이 이미 있습니다.");

        Rating rating = RatingConverter.toRating(findUser, exhibition, ratingReq.getRate());
        ratingRepository.save(rating);

        Double updateRate = calculateRate(exhibition);
        exhibition.updateRate(updateRate);

        alarmService.makeRatingAlarm(rating);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("별점을 등록했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 별점 수정
    @Transactional
    public ResponseEntity<?> updateRating(UserPrincipal userPrincipal, RatingRequestDto.RatingReq ratingReq) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Exhibition exhibition = exhibitionService.validateExhibitionById(ratingReq.getExhibitionId());

        Rating findRating = validateRatingByUserIdAndExhibitionId(findUser.getId(), exhibition.getId());
        findRating.updateRate(ratingReq.getRate());

        Double updateRate = calculateRate(exhibition);
        exhibition.updateRate(updateRate);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("별점을 수정했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 별점 삭제
    @Transactional
    public ResponseEntity<?> deleteRating(UserPrincipal userPrincipal, Long exhibitionId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Exhibition exhibition = exhibitionService.validateExhibitionById(exhibitionId);

        Rating findRating = validateRatingByUserIdAndExhibitionId(findUser.getId(), exhibition.getId());
        ratingRepository.delete(findRating);

        Double updateRate = calculateRate(exhibition);
        exhibition.updateRate(updateRate);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("별점을 삭제했습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // 나의 별점 목록 조회
    public ResponseEntity<?> getMyRatingList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);

        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Rating> ratingPage = ratingRepository.findByUserId(pageRequest, findUser.getId());
        List<Rating> ratingList = ratingPage.getContent();

        DefaultAssert.isTrue(!ratingList.isEmpty(), "내가 작성한 별점이 존재하지 않습니다.");

        List<RatingResponseDto.RatingRes> ratingRes = RatingConverter.toRatingRes(ratingList);

        Long totalElements = ratingPage.getTotalElements();
        Integer numRating = totalElements.intValue();
        boolean hasNextPage = ratingPage.hasNext();

        RatingResponseDto.RatingListRes ratingListRes = RatingConverter.toRatingListRes(numRating, hasNextPage, ratingRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(ratingListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 타 유저의 별점 목록 조회
    public ResponseEntity<?> getUserRatingList(Integer page, UserPrincipal userPrincipal, Long userId) {
        userService.validateUserByToken(userPrincipal);
        User findUser = userService.validateUserById(userId);

        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Rating> ratingPage = ratingRepository.findByUserId(pageRequest, findUser.getId());
        List<Rating> ratingList = ratingPage.getContent();

        DefaultAssert.isTrue(!ratingList.isEmpty(), "해당 유저가 작성한 별점이 존재하지 않습니다.");

        List<RatingResponseDto.RatingRes> ratingRes = RatingConverter.toRatingRes(ratingList);

        Long totalElements = ratingPage.getTotalElements();
        Integer numRating = totalElements.intValue();
        boolean hasNextPage = ratingPage.hasNext();

        RatingResponseDto.RatingListRes ratingListRes = RatingConverter.toRatingListRes(numRating, hasNextPage, ratingRes);

        ApiResponse apiResponse = ApiResponse.toApiResponse(ratingListRes);
        return ResponseEntity.ok(apiResponse);
    }

    // 전시회의 평균 별점 업데이트
    private Double calculateRate(Exhibition exhibition) {
        List<Rating> ratingList = ratingRepository.findAllByExhibitionId(exhibition.getId());

        if (ratingList.isEmpty())
            return null;

        Double sum = 0.0;
        for (Rating rating : ratingList) {
            sum += rating.getRate();
        }
        return sum / ratingList.size();
    }

    public Rating validateRatingByUserIdAndExhibitionId(Long userId, Long exhibitionId) {
        Optional<Rating> rating = ratingRepository.findByUserIdAndExhibitionId(userId, exhibitionId);
        DefaultAssert.isTrue(rating.isPresent(), "해당하는 별점이 없습니다.");
        return rating.get();
    }
}
