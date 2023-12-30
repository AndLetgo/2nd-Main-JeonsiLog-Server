package depth.jeonsilog.domain.calendar.domain.repository;

import depth.jeonsilog.domain.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByPhotoDate(LocalDate date);
}
