package depth.jeonsilog.domain.place.domain.repository;

import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String placeName);

    Optional<Place> findByAddress(String placeAddr);

    Page<Place> findByNameContainingOrAddressContaining(Pageable pageable, String searchWord, String searchWord2);

    Slice<Place> findSliceByNameContainingOrAddressContaining(Pageable pageable, String searchWord, String searchWord2);
}
