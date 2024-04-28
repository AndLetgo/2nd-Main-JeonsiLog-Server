package depth.jeonsilog.domain.user.domain;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.calendar.domain.Calendar;
import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.report.domain.Report;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Email(message = "이메일 형식이어야 합니다.")
    private String email;

    private String profileImg;

    // 포토 캘린더 공개 여부
    private Boolean isOpen;

    private Boolean isRecvExhibition;

    private Boolean isRecvActive;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    // 카카오 고유 ID
    private String providerId;

    @Nullable
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    // CASCADE 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follow", cascade = CascadeType.REMOVE)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Alarm> alarms = new ArrayList<>();

    // update 메서드
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void updateIsRecvExhibition(boolean isRecvFollowing) {
        this.isRecvExhibition = isRecvFollowing;
    }

    public void updateIsRecvActive(boolean isRecvActive) {
        this.isRecvActive = isRecvActive;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Builder
    public User(Long id, String password, String nickname, String email, String profileImg, Boolean isOpen, Boolean isRecvExhibition, Boolean isRecvActive, Role role, Provider provider, String providerId, @Nullable String fcmToken, UserLevel userLevel) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.isOpen = isOpen;
        this.isRecvExhibition = isRecvExhibition;
        this.isRecvActive = isRecvActive;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.fcmToken = fcmToken;
        this.userLevel = userLevel;
    }
}
