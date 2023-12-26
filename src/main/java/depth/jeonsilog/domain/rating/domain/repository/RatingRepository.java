package depth.jeonsilog.domain.rating.domain.repository;

import depth.jeonsilog.domain.rating.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    List<Rating> findAllByUserId(Long userId);

    List<Rating> findAllByExhibitionId(Long exhibitionId);
}
