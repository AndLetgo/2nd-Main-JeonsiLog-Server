package depth.jeonsilog.domain.alarm.domain.repository;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.interest.domain.Interest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Boolean existsByUserIdAndTargetIdAndContents(Long userId, Long targetId, String contents);

    Page<Alarm> findByUserIdAndAlarmTypeIn(PageRequest pageRequest, Long userId, List<AlarmType> types);
}
