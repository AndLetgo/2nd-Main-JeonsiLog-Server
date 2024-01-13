package depth.jeonsilog.domain.review.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@Entity
@Getter
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @Lob
    private String contents;

    private Integer numReply;

    // update 메소드
    public void updateNumReply(Integer numReply) {
        this.numReply = numReply;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    @Builder
    public Review(Long id, User user, Exhibition exhibition, String contents) {
        this.id = id;
        this.user = user;
        this.exhibition = exhibition;
        this.contents = contents;
        this.numReply = 0;
    }
}
