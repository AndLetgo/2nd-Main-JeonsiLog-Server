package depth.jeonsilog.domain.calendar.presentation;

import depth.jeonsilog.domain.calendar.application.CalendarService;
import depth.jeonsilog.domain.calendar.dto.CalendarRequestDto;
import depth.jeonsilog.domain.calendar.dto.CalendarResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Calendar API", description = "포토 캘린더 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "갤러리에서 이미지 업로드", description = "갤러리에서 이미지를 불러와서 업로드 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "업로드 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "업로드 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UploadImageReq를 확인해주세요.", required = true) @RequestPart("uploadImageReq") CalendarRequestDto.UploadImageReq uploadImageReq,
            @Parameter(description = "프로필 이미지 파일을 입력해주세요.") @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        return calendarService.uploadImage(userPrincipal, uploadImageReq, img);
    }

    @Operation(summary = "전시회 포스터에서 이미지 업로드", description = "전시회 포스터를 불러와서 업로드 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "업로드 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "업로드 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/exhibition")
    public ResponseEntity<?> uploadPoster(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UploadPosterReq 를 확인해주세요.", required = true) @RequestBody CalendarRequestDto.UploadPosterReq uploadPosterReq
    ) {
        return calendarService.uploadPoster(userPrincipal, uploadPosterReq);
    }

    @Operation(summary = "이미지 삭제", description = "해당 날짜의 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteCalendar(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UploadImageReq 를 확인해주세요.", required = true) @RequestBody CalendarRequestDto.UploadImageReq deleteImageReq
    ) {
        return calendarService.deleteCalendar(userPrincipal, deleteImageReq);
    }

    @Operation(summary = "나의 월별 이미지 목록 조회", description = "나의 해당 월의 이미지 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CalendarResponseDto.ImageRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{date}")
    public ResponseEntity<?> deleteCalendar(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "조회할 년/월을 yyyymm 형태로 입력해주세요", required = true) @PathVariable String date
    ) {
        return calendarService.getMyPhotoCalendar(userPrincipal, date);
    }

    @Operation(summary = "타 유저의 월별 이미지 목록 조회", description = "타 유저의 해당 월의 이미지 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CalendarResponseDto.ImageRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{userId}/{date}")
    public ResponseEntity<?> deleteCalendar(
            @Parameter(description = "조회할 유저의 id를 입력해주세요", required = true) @PathVariable Long userId,
            @Parameter(description = "조회할 년/월을 yyyymm 형태로 입력해주세요", required = true) @PathVariable String date
    ) {
        return calendarService.getUserPhotoCalendar(userId, date);
    }
}
