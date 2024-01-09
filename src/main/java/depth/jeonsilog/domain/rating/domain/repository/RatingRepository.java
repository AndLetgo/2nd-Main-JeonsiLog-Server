package depth.jeonsilog.domain.rating.domain.repository;

import depth.jeonsilog.domain.rating.domain.Rating;
import depth.jeonsilog.domain.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    List<Rating> findAllByUserId(Long userId);

    List<Rating> findAllByExhibitionId(Long exhibitionId);

    Page<Rating> findByUserId(PageRequest pageRequest, Long userId);
}
