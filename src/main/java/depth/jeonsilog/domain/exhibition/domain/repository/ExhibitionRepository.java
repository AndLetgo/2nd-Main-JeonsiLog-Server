package depth.jeonsilog.domain.exhibition.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
}
