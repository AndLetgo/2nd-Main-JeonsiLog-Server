package depth.jeonsilog.domain.alarm.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 알림 생성자(송신자)의 유저 id를 저장한다. 전시회 알림에는 필요하지 않기 때문에 Nullable로 설정한다. 추후 리팩토링 가능성 있음
    @Nullable
    private Long senderId;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private Long targetId;

    private Long clickId;

    // 전시회 알림의 내용만 저장한다. (7,3,1일) 나머지 알림은 유저 닉네임의 변경 가능성 때문에 저장하지 않음
    @Nullable
    private String contents;

    private Boolean isChecked = false;

    public void updateIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Builder
    public Alarm(Long id, User user, @Nullable Long senderId, AlarmType alarmType, Long targetId, Long clickId, @Nullable String contents, Boolean isChecked) {
        this.id = id;
        this.user = user;
        this.senderId = senderId;
        this.alarmType = alarmType;
        this.targetId = targetId;
        this.clickId = clickId;
        this.contents = contents;
        this.isChecked = isChecked;
    }
}
