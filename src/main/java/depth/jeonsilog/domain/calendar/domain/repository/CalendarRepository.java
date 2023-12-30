package depth.jeonsilog.domain.calendar.domain.repository;

import depth.jeonsilog.domain.calendar.domain.Calendar;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByUserAndPhotoDate(User user, LocalDate date);
}
