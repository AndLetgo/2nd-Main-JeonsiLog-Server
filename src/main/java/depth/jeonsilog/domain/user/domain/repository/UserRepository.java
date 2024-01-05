package depth.jeonsilog.domain.user.domain.repository;

import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmailAndStatus(String email, Status status);

    Boolean existsByNickname(String nickname);

    List<User> findAllByNicknameContaining(String searchWord);
}
