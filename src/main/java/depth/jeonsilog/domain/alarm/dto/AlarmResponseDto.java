package depth.jeonsilog.domain.alarm.dto;

import depth.jeonsilog.domain.alarm.domain.AlarmType;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class AlarmResponseDto {

    @Data
    @Builder
    public static class AlarmRes {

        @Schema(type = "long", example = "1", description = "알림 ID를 출력합니다.")
        private Long alarmId;

        @Schema(type = "enum", example = "REVIEW", description = "알림 타입을 출력합니다.")
        private AlarmType alarmType;

        @Schema(type = "long", example = "1", description = "각 알림 타입에 따라 감상평ID, 댓글ID 등을 targetId 로 설정한다.")
        private Long targetId;

        @Schema(type = "long", example = "1", description = "클릭 시 페이지 이동에 필요한 ID를 clickId 로 설정한다.")
        private Long clickId;

        @Schema(type = "string", example = "https://..", description = "프로필 / 전시회 이미지 url을 출력한다.")
        private String imgUrl;

        @Schema(type = "string", example = "루피 님이 나를 팔로우해요", description = "알림 내용을 출력합니다.")
        private String contents;

        @Schema(type = "LocalDateTime", example = "2023-12-22 23:51:45.848882", description = "알림이 생성된 DateTime을 출력합니다.")
        private LocalDateTime dateTime;

        @Schema(type = "boolean", example = "true", description = "알림을 확인했는지 출력합니다.")
        private Boolean isChecked;
    }

    @Data
    @Builder
    public static class AlarmResListWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<AlarmResponseDto.AlarmRes> data;
    }
}
