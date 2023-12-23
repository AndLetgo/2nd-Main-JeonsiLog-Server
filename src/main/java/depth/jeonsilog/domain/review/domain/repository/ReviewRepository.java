package depth.jeonsilog.domain.review.domain.repository;

import depth.jeonsilog.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByExhibitionId(Long exhibitionId);

    List<Review> findAllByUserId(Long userId);
}
