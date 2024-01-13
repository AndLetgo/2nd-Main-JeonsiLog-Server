package depth.jeonsilog.domain.user.presentation;

import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.dto.UserRequestDto;
import depth.jeonsilog.domain.user.dto.UserResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import depth.jeonsilog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Tag(name = "User API", description = "User 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "AccessToken을 이용하여 본인 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.UserRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findUserByToken(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUserByToken(userPrincipal);
    }

    @Operation(summary = "타 유저 정보 조회", description = "타 유저의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.UserRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> findUser(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "User Id를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return userService.findUserById(userPrincipal, userId);
    }

    @Operation(summary = "닉네임 변경", description = "본인의 닉네임을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/nickname")
    public ResponseEntity<?> changeNickname(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 ChangeNicknameReq를 참고해주세요", required = true) @Valid @RequestBody UserRequestDto.ChangeNicknameReq changeNicknameReq
    ) {
        return userService.changeNickname(userPrincipal, changeNicknameReq);
    }

    @Operation(summary = "프로필 변경", description = "본인의 프로필을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> changeProfile(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "프로필 이미지 파일을 입력해주세요.") @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        return userService.changeProfile(userPrincipal, img);
    }

    @Operation(summary = "유저 검색", description = "검색어로 유저를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SearchUserWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchUsers(
            @Parameter(description = "유저 검색 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 를 참고해주세요", required = true) @PathVariable(value = "searchWord") String keyword
    ) {
        return userService.searchUsers(page, userPrincipal, keyword);
    }

    @Operation(summary = "포토 캘린더 공개/비공개 변경", description = "포토 캘린더 공개/비공개 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SwitchIsOpenRes.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/calendar")
    public ResponseEntity<?> switchIsOpen(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsOpen(userPrincipal);
    }

    @Operation(summary = "전시 알림 수신 여부 변경", description = "전시 알림 수신 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SwitchIsRecvExhibitionRes.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/alarm-exhibition")
    public ResponseEntity<?> switchIsRecvExhibition(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsRecvExhibition(userPrincipal);
    }

    @Operation(summary = "활동 알림 수신 여부 변경", description = "활동 알림 수신 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SwitchIsRecvActiveRes.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/alarm-active")
    public ResponseEntity<?> switchIsRecvActive(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsRecvActive(userPrincipal);
    }

    @Operation(summary = "회원 탈퇴", description = "회원이 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "탈퇴 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.deleteUser(userPrincipal);
    }

    @Operation(summary = "타 유저 포토 캘린더 공개 여부 조회", description = "User Id로 포토 캘린더 공개 여부를 조회합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.IsOpenRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/calendar/{userId}")
    public ResponseEntity<?> getIsOpen(
            @Parameter(description = "조회할 유저의 ID를 입력해주세요", required = true) @PathVariable Long userId
    ) {
        return userService.getIsOpen(userId);
    }

    @Operation(summary = "전시 및 활동 알림 수신 여부 조회", description = "AccessToken을 이용하여 전시 및 활동 알림 수신 여부를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.IsRecvOrNotRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/reception")
    public ResponseEntity<?> getRecvOrNot(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.getRecvOrNot(userPrincipal);
    }


    @Operation(summary = "Fcm Token 변경", description = "Fcm Token을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/fcm/token")
    public ResponseEntity<?> updateFcmToken(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "UpdateFcmToken 을 확인해주세요", required = true) @RequestBody UserRequestDto.UpdateFcmToken fcmToken
            ) {
        return userService.updateFcmToken(userPrincipal, fcmToken);
    }
}
