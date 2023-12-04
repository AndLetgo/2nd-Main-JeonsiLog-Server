package depth.jeonsilog.domain.exhibition.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.place.domain.Place;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private OperatingKeyword operatingKeyword;

    @Enumerated(EnumType.STRING)
    private PriceKeyword priceKeyword;

    private String price;

    private String startDate;

    private String endDate;

    @Lob
    private String information;

    private double rate;

    private int sequence;

    @Builder
    public Exhibition(Long id, Place place, String name, OperatingKeyword operatingKeyword, PriceKeyword priceKeyword, String price, String startDate, String endDate, String information, double rate) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.operatingKeyword = operatingKeyword;
        this.priceKeyword = priceKeyword;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.information = information;
        this.rate = rate;
        this.sequence = 0; // default = 0
    }
}
