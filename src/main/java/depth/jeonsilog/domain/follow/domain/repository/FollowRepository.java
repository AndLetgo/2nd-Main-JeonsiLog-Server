package depth.jeonsilog.domain.follow.domain.repository;

import depth.jeonsilog.domain.follow.domain.Follow;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByUserAndFollow(User user, User follow);

    List<Follow> findAllByUser(User user);

    List<Follow> findAllByFollow(User followUser);

    Integer countByUser(User user);

    Integer countByFollow(User user);

    Slice<Follow> findSliceByUser(PageRequest pageRequest, User user);

    Slice<Follow> findSliceByFollow(PageRequest pageRequest, User followUser);
}
