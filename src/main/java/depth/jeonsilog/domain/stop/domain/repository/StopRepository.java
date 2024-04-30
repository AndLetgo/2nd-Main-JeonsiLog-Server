package depth.jeonsilog.domain.stop.domain.repository;

import depth.jeonsilog.domain.stop.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop, Long> {

    Optional<Stop> findByUserId(Long userId);
}
