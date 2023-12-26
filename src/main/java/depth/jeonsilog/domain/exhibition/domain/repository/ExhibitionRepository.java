package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    @EntityGraph(attributePaths = {"place"})
    Page<Exhibition> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"place"})
    Page<Exhibition> findByNameContaining(Pageable pageable, String searchWord);

    Page<Exhibition> findByPlace(Pageable pageable, Place place);

}
