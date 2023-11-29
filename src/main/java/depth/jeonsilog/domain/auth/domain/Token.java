package depth.jeonsilog.domain.auth.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Token extends BaseEntity {

    @Id
    @Column(name = "user_email" ,nullable = false)
    private String userEmail;

    @Lob
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }
}
