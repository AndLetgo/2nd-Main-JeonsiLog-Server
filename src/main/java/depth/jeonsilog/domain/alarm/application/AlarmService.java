package depth.jeonsilog.domain.alarm.application;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.alarm.domain.repository.AlarmRepository;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.follow.domain.repository.FollowRepository;
import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final FollowRepository followRepository;

    // TODO: 팔로잉한 사람의 new 감상평 -> 알림 생성
    @Transactional
    public void makeReviewAlarm(Review review) {
        List<Follow> follows = followRepository.findAllByFollow(review.getUser());
        for (Follow follow : follows) {
            Alarm alarm = Alarm.builder()
                    .user(follow.getUser())
                    .alarmType(AlarmType.REVIEW)
                    .targetId(review.getId())
                    .build();
            alarmRepository.save(alarm);
        }
    }

    // 팔로잉한 사람의 new 별점 -> 알림 생성
    @Transactional
    public void makeRatingAlarm(Rating rating) {
        List<Follow> follows = followRepository.findAllByFollow(rating.getUser());
        for (Follow follow : follows) {
            Alarm alarm = Alarm.builder()
                    .user(follow.getUser())
                    .alarmType(AlarmType.RATING)
                    .targetId(rating.getId())
                    .build();
            alarmRepository.save(alarm);
        }
    }

    // 나의 감상평에 달린 댓글 -> 알림 생성
    @Transactional
    public void makeReplyAlarm(Reply reply) {
        Alarm alarm = Alarm.builder()
                .user(reply.getReview().getUser())
                .alarmType(AlarmType.REPLY)
                .targetId(reply.getId())
                .build();
        alarmRepository.save(alarm);
    }

    // 나를 팔로우 -> 알림 생성
    @Transactional
    public void makeFollowAlarm(Follow follow) {
        Alarm alarm = Alarm.builder()
                .user(follow.getFollow())
                .alarmType(AlarmType.FOLLOW)
                .targetId(follow.getId())
                .build();
        alarmRepository.save(alarm);
    }
}
