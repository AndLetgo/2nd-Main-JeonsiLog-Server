package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    @EntityGraph(attributePaths = {"place"})
    Page<Exhibition> findAll(Pageable pageable);

    List<Exhibition> findByNameContaining(String searchWord);

}
