package depth.jeonsilog.domain.alarm.converter;

import depth.jeonsilog.domain.alarm.domain.Alarm;
import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.alarm.dto.AlarmResponseDto;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;

import java.util.List;

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

    public static AlarmResponseDto.AlarmResListWithPaging toAlarmResListWithPaging(boolean hasNextPage, List<AlarmResponseDto.AlarmRes> alarmResList) {
        return AlarmResponseDto.AlarmResListWithPaging.builder()
                .hasNextPage(hasNextPage)
                .data(alarmResList)
                .build();
    }
}
