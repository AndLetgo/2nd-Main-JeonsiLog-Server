package depth.jeonsilog.domain.interest.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.rating.domain.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    List<Interest> findAllByUserId(Long userId);

    List<Interest> findByExhibition_OperatingKeyword(OperatingKeyword operatingKeyword);

    Page<Interest> findByUserId(PageRequest pageRequest, Long userId);
}
