package depth.jeonsilog.domain.reply.domain.repository;

import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Slice<Reply> findSliceByReview(PageRequest pageRequest, Review review);

    List<Reply> findByReview(Review review);

    @Query(value = "SELECT * FROM reply WHERE review_id = :reviewId", nativeQuery = true)
    List<Reply> findAllRepliesByReviewId(@Param("reviewId") Long reviewId);

    @Query(value = "SELECT * FROM reply WHERE user_id = :userId", nativeQuery = true)
    List<Reply> findAllRepliesByUserId(@Param("userId") Long userId);

}
