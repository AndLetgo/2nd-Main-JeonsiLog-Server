package depth.jeonsilog.domain.image.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Image extends BaseEntity {

    /**
     * TODO : 이미지 원본 이름 있어야 하려나.. ? --> Image 테이블의 ID와 연관지어 생각
     * TODO : s3 controller, service 는 image의 repository, entity와 연결될 필요가 있다
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    private String imageUrl;

    @Builder
    public Image(Long id, Exhibition exhibition, String imageUrl) {
        this.id = id;
        this.exhibition = exhibition;
        this.imageUrl = imageUrl;
    }
}
