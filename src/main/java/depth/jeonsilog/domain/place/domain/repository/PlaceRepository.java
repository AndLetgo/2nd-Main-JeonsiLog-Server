package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String placeName);

    Optional<Place> findByAddress(String placeAddr);

}
