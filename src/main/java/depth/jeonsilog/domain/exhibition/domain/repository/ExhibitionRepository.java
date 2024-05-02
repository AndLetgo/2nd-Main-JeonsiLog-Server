package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.place.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    // 2주 이하
    @Query(value = "SELECT * FROM exhibition e WHERE DATEDIFF(STR_TO_DATE(e.end_date, '%Y%m%d'), CURDATE()) < 14 AND CURDATE() <= STR_TO_DATE(e.end_date, '%Y%m%d')", nativeQuery = true)
    List<Exhibition> findExhibitionsWithinTwoWeeks();

    // 8일 미만
    @Query(value = "SELECT * FROM exhibition e WHERE DATEDIFF(CURDATE(), STR_TO_DATE(e.start_date, '%Y%m%d')) < 8 AND CURDATE() >= STR_TO_DATE(e.start_date, '%Y%m%d')", nativeQuery = true)
    List<Exhibition> findRecentExhibitions();

    @Query(value = "SELECT e FROM Exhibition e JOIN e.place p WHERE p.address LIKE %:keyword%")
    List<Exhibition> findExhibitionsByAddressContainingKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT e FROM Exhibition e JOIN e.place p WHERE p.name LIKE %:keyword%")
    List<Exhibition> findExhibitionsByNameContainingKeyword(@Param("keyword") String keyword);
}
