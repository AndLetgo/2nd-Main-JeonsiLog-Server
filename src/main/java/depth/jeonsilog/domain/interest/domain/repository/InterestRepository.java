package depth.jeonsilog.domain.interest.domain.repository;

import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.interest.domain.Interest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>, InterestQuerydslRepository {

    Optional<Interest> findByUserIdAndExhibitionId(Long userId, Long exhibitionId);

    List<Interest> findAllByUserId(Long userId);

    List<Interest> findByExhibition_OperatingKeyword(OperatingKeyword operatingKeyword);

    Slice<Interest> findSliceByUserId(PageRequest pageRequest, Long userId);
}
