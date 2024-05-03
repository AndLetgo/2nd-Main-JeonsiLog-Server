package depth.jeonsilog.domain.stop.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Stop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_first_access")
    private Boolean isFirstAccess;

    public void updateIsFirstAccess() {
        this.isFirstAccess = false;
    }

    @Builder
    public Stop(final User user, final String reason) {
        this.user = user;
        this.reason = reason;
        this.isFirstAccess = true;
    }
}
