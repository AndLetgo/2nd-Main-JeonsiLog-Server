package depth.jeonsilog.domain.user.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private boolean isOpen;

    private boolean isRecvFollowing;

    private boolean isRecvActive;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    // 카카오 고유 ID
    private String providerId;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Builder
    public User(Long id, String password, String nickname, String email, String providerId, String profileImg, boolean isOpen, boolean isRecvFollowing, boolean isRecvActive, Role role, Provider provider) {
        this.id = id;
        this.password = password;
        this.providerId = providerId;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.isOpen = isOpen;
        this.isRecvFollowing = isRecvFollowing;
        this.isRecvActive = isRecvActive;
        this.role = role;
        this.provider = provider;
    }
}
