package depth.jeonsilog.domain.alarm.converter;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.interest.domain.Interest;

public class AlarmConverter {

    public static Alarm toExhibitionAlarm(Interest interest, String contents) {
        return Alarm.builder()
                .user(interest.getUser())
                .alarmType(AlarmType.EXHIBITION)
                .targetId(interest.getExhibition().getId())
                .clickId(interest.getExhibition().getId())
                .contents(contents)
                .isChecked(false)
                .build();
    }
}
