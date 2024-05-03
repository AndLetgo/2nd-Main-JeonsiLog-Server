package depth.jeonsilog.domain.calendar.converter;

import depth.jeonsilog.domain.calendar.domain.Calendar;
import depth.jeonsilog.domain.calendar.dto.CalendarResponseDto;
import depth.jeonsilog.domain.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarConverter {

    public static Calendar toCalendar(User user, LocalDate date, String imgUrl, String caption) {
        return Calendar.builder()
                .user(user)
                .photoDate(date)
                .imageUrl(imgUrl)
                .caption(caption)
                .build();
    }

    public static List<CalendarResponseDto.ImageRes> toImageRes(List<Calendar> calendarList) {
        List<CalendarResponseDto.ImageRes> imageResList = new ArrayList<>();

        for (Calendar calendar : calendarList) {
            CalendarResponseDto.ImageRes imageRes = CalendarResponseDto.ImageRes.builder()
                    .calendarId(calendar.getId())
                    .date(calendar.getPhotoDate())
                    .imgUrl(calendar.getImageUrl())
                    .caption(calendar.getCaption())
                    .build();

            imageResList.add(imageRes);
        }
        return imageResList;
    }
}
