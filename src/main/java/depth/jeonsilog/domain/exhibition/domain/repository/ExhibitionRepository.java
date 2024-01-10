package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    @EntityGraph(attributePaths = {"place"})
    Slice<Exhibition> findSliceBy(Pageable pageable);

    Page<Exhibition> findByNameContaining(Pageable pageable, String searchWord);

    Slice<Exhibition> findSliceByNameContaining(Pageable pageable, String searchWord);

    Slice<Exhibition> findSliceByNameContainingOrPlace_AddressContaining(Pageable pageable, String searchWord, String searchWord2);

    Page<Exhibition> findByPlace(Pageable pageable, Place place);

    Slice<Exhibition> findSliceByPlace(Pageable pageable, Place place);

    Optional<Exhibition> findBySequence(Integer sequence);

    Optional<Exhibition> findByExhibitionSeq(Integer exhibitionSeq);
}
