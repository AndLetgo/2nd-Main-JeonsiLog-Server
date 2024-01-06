package depth.jeonsilog.domain.rating.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Rating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    private Double rate;

    public void updateRate(Double rate) {
        this.rate = rate;
    }

    // 유저 삭제 시 사용 (null)
    public void deleteUser() {
        this.user = null;
    }

    @Builder
    public Rating(Long id, User user, Exhibition exhibition, Double rate) {
        this.id = id;
        this.user = user;
        this.exhibition = exhibition;
        this.rate = rate;
    }
}
