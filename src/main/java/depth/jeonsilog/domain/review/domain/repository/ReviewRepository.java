package depth.jeonsilog.domain.review.domain.repository;

import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByUserId(Long userId);

    Boolean existsByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    Page<Review> findByExhibitionId(PageRequest pageRequest, Long exhibitionId);

    Page<Review> findByUserId(PageRequest pageRequest, Long userId);
}
