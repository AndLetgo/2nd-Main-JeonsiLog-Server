package depth.jeonsilog.domain.review.domain.repository;

import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * FROM review WHERE user_id = :userId", nativeQuery = true)
    List<Review> findAllReviewsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM review WHERE id = :reviewId", nativeQuery = true)
    Review findReviewByReviewId(@Param("reviewId") Long reviewId);

    Optional<Review> findByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    Page<Review> findByUserId(PageRequest pageRequest, Long userId);

    Slice<Review> findSliceByExhibitionId(PageRequest pageRequest, Long exhibitionId);

    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);
}
