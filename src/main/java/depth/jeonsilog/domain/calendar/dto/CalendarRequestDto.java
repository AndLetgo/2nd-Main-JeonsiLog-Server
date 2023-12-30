package depth.jeonsilog.domain.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public class CalendarRequestDto
{
    @Data
    public static class UploadImageReq {

        @Schema(type = "local date", example = "2023-12-25", description = "이미지가 업로드될 날짜")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        private LocalDate date;
    }

    @Data
    public static class UploadPosterReq {

        @Schema(type = "local date", example = "2023-12-25", description = "이미지가 업로드될 날짜")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        private LocalDate date;

        @Schema(type = "string", example = "https://jeonsi-s3", description = "전시회 포스터 이미지 url")
        @NotNull
        private String imgUrl;
    }
}
