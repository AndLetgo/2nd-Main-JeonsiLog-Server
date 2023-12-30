package depth.jeonsilog.domain.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class CalendarResponseDto {

    @Data
    @Builder
    public static class ImageRes {

        @Schema(type = "long", example = "1", description = "캘린더 이미지 id")
        private Long calendarId;

        @Schema(type = "local date", example = "2023-12-25", description = "이미지가 등록된 날짜")
        private LocalDate date;

        @Schema(type = "string", example = "https://jeonsi-s3", description = "이미지 url")
        private String imgUrl;
    }
}
