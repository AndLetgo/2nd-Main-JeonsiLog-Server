package depth.jeonsilog.domain.user.presentation;

import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.dto.UserRequestDto;
import depth.jeonsilog.domain.user.dto.UserResponseDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "User 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //-✅ (토큰을 이용한) (본인)유저 정보 조회 : GET /users findUser
    //-✅ 타유저 정보 조회 : GET /users/{user-id}
    //-✅ 닉네임 변경 : PATCH /users/nickname
    //- 프로필 사진 변경 : PATCH /users/profile
    //-✅ 검색어를 포함한 사용자 목록 조회 : GET /users/search/{search-word}
    //- 포토 캘린더 공개/비공개 변경 : PATCH /users/calendar
    //- 팔로잉 알림 수신 여부 변경 : PATCH /users/alarm-following
    //- 나의 활동 알림 수신 여부 변경 : PATCH /users/alarm-activity
    //- (토큰) 회원 탈퇴 : DELETE /users

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
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.UserRes.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/nickname")
    public ResponseEntity<?> changeNickname(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 ChangeNicknameReq를 참고해주세요", required = true) @Valid @RequestBody UserRequestDto.ChangeNicknameReq changeNicknameReq
    ) {
        return userService.changeNickname(userPrincipal, changeNicknameReq);
    }

    @Operation(summary = "유저 검색", description = "검색어로 유저를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SearchUsersRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<?> searchUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 를 참고해주세요", required = true) @PathVariable(value = "searchWord") String keyword
    ) {
        return userService.searchUsers(userPrincipal, keyword);
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

    @Operation(summary = "팔로잉 알림 수신 여부 변경", description = "팔로잉 알림 수신 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.SwitchIsRecvFollowingRes.class))}),
            @ApiResponse(responseCode = "400", description = "변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/alarm-following")
    public ResponseEntity<?> switchIsRecvFollowing(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.switchIsRecvFollowing(userPrincipal);
    }

    @Operation(summary = "나의 활동 알림 수신 여부 변경", description = "나의 활동 알림 수신 여부를 변경합니다.")
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
}
