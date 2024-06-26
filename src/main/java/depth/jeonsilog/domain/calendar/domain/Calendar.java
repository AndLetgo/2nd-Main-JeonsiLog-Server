package depth.jeonsilog.domain.calendar.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate photoDate;

    @Column(length = 2048)
    private String imageUrl;

    private String caption;

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Builder
    public Calendar(Long id, User user, LocalDate photoDate, String imageUrl, String caption) {
        this.id = id;
        this.user = user;
        this.photoDate = photoDate;
        this.imageUrl = imageUrl;
        this.caption = caption;
    }
}
