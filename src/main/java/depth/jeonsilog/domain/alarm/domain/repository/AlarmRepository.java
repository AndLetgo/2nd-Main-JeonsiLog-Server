package depth.jeonsilog.domain.alarm.domain.repository;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUserIdAndAlarmTypeIn(Long userId, List<AlarmType> types);

    Boolean existsByUserIdAndTargetIdAndContents(Long userId, Long targetId, String contents);
}
