package depth.jeonsilog.domain.reply.domain.repository;

import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findByReview(PageRequest pageRequest, Review review);

    List<Reply> findByReview(Review review);

    List<Reply> findAllByUserId(Long userId);

}
