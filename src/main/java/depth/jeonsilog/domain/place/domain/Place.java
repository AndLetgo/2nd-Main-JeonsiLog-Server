package depth.jeonsilog.domain.place.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.place.dto.PlaceRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String address;

    private String operatingTime;

    @Enumerated(EnumType.STRING)
    private ClosedDays closedDays;

    private String tel;

    private String homePage;

    // update 메소드
    public void updatePlaceWithExhibitionDetail(PlaceRequestDto.UpdatePlaceWithExhibitionDetailReq updatePlaceWithExhibitionDetailReq) {
        this.name = updatePlaceWithExhibitionDetailReq.getPlaceName();
        this.address = updatePlaceWithExhibitionDetailReq.getPlaceAddress();
        this.tel = updatePlaceWithExhibitionDetailReq.getPlaceTel();
        this.homePage = updatePlaceWithExhibitionDetailReq.getPlaceHomePage();
    }

    public void updateTel(String tel) {
        this.tel = tel;
    }

    @Builder
    public Place(Long id, String name, String address, String operatingTime, ClosedDays closedDays, String tel, String homePage) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.operatingTime = operatingTime;
        this.closedDays = closedDays;
        this.tel = tel;
        this.homePage = homePage;
    }
}
