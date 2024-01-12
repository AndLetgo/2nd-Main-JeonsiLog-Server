package depth.jeonsilog.domain.alarm.application;

import depth.jeonsilog.domain.alarm.converter.AlarmConverter;
import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.alarm.domain.repository.AlarmRepository;
import depth.jeonsilog.domain.alarm.dto.AlarmResponseDto;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.fcm.application.FcmService;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.repository.InterestRepository;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final FollowRepository followRepository;
    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;
    private final FcmService fcmService;

    private static String BEFORE_7DAYS = "전시 시작까지 7일 남았어요";
    private static String BEFORE_3DAYS = "전시 시작까지 3일 남았어요";
    private static String BEFORE_1DAYS = "전시 시작까지 1일 남았어요";

    // TODO: 활동 알림 목록 조회
    public ResponseEntity<?> getActivityAlarmList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<AlarmType> types = Arrays.asList(AlarmType.RATING, AlarmType.REVIEW, AlarmType.REPLY, AlarmType.FOLLOW);

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Alarm> alarmPage = alarmRepository.findSliceByUserIdAndAlarmTypeIn(pageRequest, findUser.getId(), types);
        List<Alarm> alarmList = alarmPage.getContent();

        DefaultAssert.isTrue(!alarmList.isEmpty(), "활동 알림이 존재하지 않습니다.");

        List<AlarmResponseDto.AlarmRes> alarmResList = new ArrayList<>();
        for (Alarm alarm : alarmList) {
            Optional<User> sender = userRepository.findById(alarm.getSenderId());
            DefaultAssert.isTrue(sender.isPresent());

            String content = "";
            switch (alarm.getAlarmType()) {
                case REVIEW -> content = sender.get().getNickname() + " 님이 감상평을 남겼어요";
                case RATING -> content = sender.get().getNickname() + " 님이 별점을 남겼어요";
                case REPLY -> content = sender.get().getNickname() + " 님이 내 감상평에 댓글을 남겼어요";
                case FOLLOW -> content = sender.get().getNickname() + " 님이 나를 팔로우해요";
            }

            AlarmResponseDto.AlarmRes alarmRes = AlarmResponseDto.AlarmRes.builder()
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .targetId(alarm.getTargetId())
                    .clickId(alarm.getClickId())
                    .imgUrl(sender.get().getProfileImg())
                    .contents(content)
                    .dateTime(alarm.getCreatedDate())
                    .isChecked(alarm.getIsChecked())
                    .build();
            alarmResList.add(alarmRes);
        }

        boolean hasNextPage = alarmPage.hasNext();
        AlarmResponseDto.AlarmResListWithPaging alarmResListWithPaging = AlarmConverter.toAlarmResListWithPaging(hasNextPage, alarmResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(alarmResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 전시 알림 목록 조회
    public ResponseEntity<?> getExhibitionAlarmList(Integer page, UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<AlarmType> types = List.of(AlarmType.EXHIBITION);

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Slice<Alarm> alarmPage = alarmRepository.findSliceByUserIdAndAlarmTypeIn(pageRequest, findUser.getId(), types);
        List<Alarm> alarmList = alarmPage.getContent();

        DefaultAssert.isTrue(!alarmList.isEmpty(), "전시 알림이 존재하지 않습니다.");

        List<AlarmResponseDto.AlarmRes> alarmResList = new ArrayList<>();
        for (Alarm alarm : alarmList) {
            Optional<Exhibition> exhibition = exhibitionRepository.findById(alarm.getTargetId());
            DefaultAssert.isTrue(exhibition.isPresent());
            AlarmResponseDto.AlarmRes alarmRes = AlarmResponseDto.AlarmRes.builder()
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .targetId(alarm.getTargetId())
                    .clickId(alarm.getClickId())
                    .imgUrl(exhibition.get().getImageUrl())
                    .contents("[" + exhibition.get().getName() + "] " + alarm.getContents())
                    .dateTime(alarm.getCreatedDate())
                    .isChecked(alarm.getIsChecked())
                    .build();
            alarmResList.add(alarmRes);
        }

        boolean hasNextPage = alarmPage.hasNext();
        AlarmResponseDto.AlarmResListWithPaging alarmResListWithPaging = AlarmConverter.toAlarmResListWithPaging(hasNextPage, alarmResList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(alarmResListWithPaging);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 알림 확인
    @Transactional
    public ResponseEntity<?> checkAlarm(UserPrincipal userPrincipal, Long alarmId) {

        User findUser = userService.validateUserByToken(userPrincipal);

        Optional<Alarm> alarm = alarmRepository.findById(alarmId);
        DefaultAssert.isTrue(alarm.isPresent(), "알림 정보가 올바르지 않습니다.");
        DefaultAssert.isTrue(findUser.equals(alarm.get().getUser()), "알림 수신자만 알림을 확인할 수 있습니다.");

        Alarm findAlarm = alarm.get();
        findAlarm.updateIsChecked(true);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("알림이 확인되었습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // TODO: 팔로잉한 사람의 new 감상평 -> 알림 생성
    @Transactional
    public void makeReviewAlarm(Review review) throws IOException {
        List<Follow> follows = followRepository.findAllByFollow(review.getUser());
        for (Follow follow : follows) {
            User receiver = follow.getUser();
            Alarm alarm = Alarm.builder()
                    .user(receiver)
                    .senderId(follow.getFollow().getId())
                    .alarmType(AlarmType.REVIEW)
                    .targetId(review.getId())
                    .clickId(review.getId())
                    .isChecked(false)
                    .build();
            alarmRepository.save(alarm);

            if (!receiver.getIsRecvActive() || receiver.getFcmToken() == null) return;
            fcmService.send(receiver.getFcmToken(), "전시로그", follow.getFollow().getNickname() + " 님이 감상평 남겼어요");
        }
    }

    // TODO: 팔로잉한 사람의 new 별점 -> 알림 생성
    @Transactional
    public void makeRatingAlarm(Rating rating) throws IOException {
        List<Follow> follows = followRepository.findAllByFollow(rating.getUser());
        for (Follow follow : follows) {
            User receiver = follow.getUser();
            Alarm alarm = Alarm.builder()
                    .user(receiver)
                    .senderId(follow.getFollow().getId())
                    .alarmType(AlarmType.RATING)
                    .targetId(rating.getId())
                    .clickId(rating.getExhibition().getId())
                    .isChecked(false)
                    .build();
            alarmRepository.save(alarm);

            if (!receiver.getIsRecvActive() || receiver.getFcmToken() == null) return;
            fcmService.send(receiver.getFcmToken(), "전시로그", follow.getFollow().getNickname() + " 님이 별점을 남겼어요");
        }
    }

    // TODO: 나의 감상평에 달린 댓글 -> 알림 생성
    @Transactional
    public void makeReplyAlarm(Reply reply) throws IOException {
        User receiver = reply.getReview().getUser();
        Alarm alarm = Alarm.builder()
                .user(receiver)
                .senderId(reply.getUser().getId())
                .alarmType(AlarmType.REPLY)
                .targetId(reply.getId())
                .clickId(reply.getReview().getId())
                .isChecked(false)
                .build();
        alarmRepository.save(alarm);

        if (!receiver.getIsRecvActive() || receiver.getFcmToken() == null) return;
        fcmService.send(receiver.getFcmToken(), "전시로그", reply.getUser().getNickname() + " 님이 댓글을 남겼어요");
    }

    // TODO: 나를 팔로우 -> 알림 생성
    @Transactional
    public void makeFollowAlarm(Follow follow) throws IOException {
        User receiver = follow.getFollow();
        Alarm alarm = Alarm.builder()
                .user(receiver)
                .senderId(follow.getUser().getId())
                .alarmType(AlarmType.FOLLOW)
                .targetId(follow.getId())
                .clickId(follow.getUser().getId())
                .isChecked(false)
                .build();
        alarmRepository.save(alarm);

        if (!receiver.getIsRecvActive() || receiver.getFcmToken() == null) return;
        fcmService.send(receiver.getFcmToken(), "전시로그", follow.getUser().getNickname() + " 님이 나를 팔로우해요");
    }

    // TODO: 관심 전시회 시작 전 -> 알림 생성
    @Transactional
    @Scheduled(cron = "0 0 9 * * *") // 오전 9시에 실행
    public void makeExhibitionAlarm() {
        List<Interest> interests = interestRepository.findByExhibition_OperatingKeyword(OperatingKeyword.BEFORE_DISPLAY);

        LocalDate currentDate = LocalDate.now();
        log.info("현재 날짜: " + currentDate);
        for (Interest interest : interests) {
            Exhibition exhibition = interest.getExhibition();
            User receiver = interest.getUser();

            LocalDate targetDate = LocalDate.parse(exhibition.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.info("전시회 시작 날짜: " + targetDate);

            Alarm alarm = null;
            long daysDifference = ChronoUnit.DAYS.between(currentDate, targetDate);
            if (daysDifference == 7) {
                if (checkDuplicateAlarm(receiver.getId(), exhibition.getId(), BEFORE_7DAYS)) continue;
                alarm = AlarmConverter.toExhibitionAlarm(interest, BEFORE_7DAYS);
            } else if (daysDifference == 3) {
                if (checkDuplicateAlarm(receiver.getId(), exhibition.getId(), BEFORE_3DAYS)) continue;
                alarm = AlarmConverter.toExhibitionAlarm(interest, BEFORE_3DAYS);
            } else if (daysDifference == 1) {
                if (checkDuplicateAlarm(receiver.getId(), exhibition.getId(), BEFORE_1DAYS)) continue;
                alarm = AlarmConverter.toExhibitionAlarm(interest, BEFORE_1DAYS);
            }
            assert alarm != null;
            alarmRepository.save(alarm);

            if (!receiver.getIsRecvExhibition() || receiver.getFcmToken() == null) return;
            fcmService.send(receiver.getFcmToken(), "전시로그", "[" + exhibition.getName() + "]\n" + alarm.getContents());
        }
    }

    public boolean checkDuplicateAlarm(Long userId, Long targetId, String contents) {
        return alarmRepository.existsByUserIdAndTargetIdAndContents(userId, targetId, contents);
    }
}
