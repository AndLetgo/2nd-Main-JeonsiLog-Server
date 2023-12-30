package depth.jeonsilog.domain.calendar.converter;

import depth.jeonsilog.domain.calendar.domain.Calendar;
import depth.jeonsilog.domain.user.domain.User;

import java.time.LocalDate;

public class CalendarConverter {

    public static Calendar toCalendar(User user, LocalDate date, String imgUrl) {
        return Calendar.builder()
                .user(user)
                .photoDate(date)
                .imageUrl(imgUrl)
                .build();
    }
}
