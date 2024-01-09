package depth.jeonsilog.domain.exhibition.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.exhibition.dto.ExhibitionRequestDto;
import depth.jeonsilog.domain.place.domain.Place;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Exhibition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private OperatingKeyword operatingKeyword;

    @Enumerated(EnumType.STRING)
    private PriceKeyword priceKeyword;

    private String price;

    private String startDate;

    private String endDate;

    @Lob
    private String information;

    private Double rate;

    private Integer exhibitionSeq;

    @Min(value = 1)
    @Max(value = 11)
    private Integer sequence;

    // update 메소드
    public void updateExhibitionDetail(ExhibitionRequestDto.UpdateExhibitionDetailReq updateExhibitionDetailReq, String imageUrl) {
        this.name = updateExhibitionDetailReq.getExhibitionName();
        this.operatingKeyword = updateExhibitionDetailReq.getOperatingKeyword();
        this.priceKeyword = updateExhibitionDetailReq.getPriceKeyword();
        this.information = updateExhibitionDetailReq.getInformation();

        if (updateExhibitionDetailReq.getIsImageChange()) { // 이미지 변경하는 경우
            this.imageUrl = imageUrl;
        }
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRate(Double rate) {
        this.rate = rate;
    }

    public void updateSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Builder
    public Exhibition(Long id, Place place, String name, String imageUrl, OperatingKeyword operatingKeyword, PriceKeyword priceKeyword, String price, String startDate, String endDate, String information, Double rate, Integer exhibitionSeq) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.imageUrl = imageUrl;
        this.operatingKeyword = operatingKeyword;
        this.priceKeyword = priceKeyword;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.information = information;
        this.rate = rate;
        this.sequence = 11; // default = 11
        this.imageUrl = imageUrl;
        this.exhibitionSeq = exhibitionSeq;
    }
}
