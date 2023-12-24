package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
