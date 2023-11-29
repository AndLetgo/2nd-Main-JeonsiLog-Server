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

    @Column(unique = true)
    private String nickname;

    @Email(message = "이메일 형식이어야 합니다.")
    private String email;

    private String password;

    private String profileImg;

    // 포토 캘린더 공개 여부
    private boolean isOpen = true;

    private boolean isRecvFollowing = true;

    private boolean isRecvActive = true;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String nickname, String email, String password, String profileImg, boolean isOpen, boolean isRecvFollowing, boolean isRecvActive, Role role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.isOpen = isOpen;
        this.isRecvFollowing = isRecvFollowing;
        this.isRecvActive = isRecvActive;
        this.role = role;
    }
}
