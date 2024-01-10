package depth.jeonsilog.domain.follow.presentation;

import depth.jeonsilog.domain.exhibition.dto.ExhibitionResponseDto;
import depth.jeonsilog.domain.follow.application.FollowService;
import depth.jeonsilog.domain.follow.dto.FollowResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Follow API", description = "Follow 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우", description = "User ID를 이용하여 특정 유저를 팔로우합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팔로우 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "팔로우 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{userId}")
    public ResponseEntity<?> follow(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return followService.follow(userPrincipal, userId);
    }

    @Operation(summary = "팔로우 취소", description = "User ID를 이용하여 특정 유저를 팔로우 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 취소 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "팔로우 취소 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteFollow(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우 취소할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return followService.deleteFollowing(userPrincipal, userId);
    }

    //- 팔로워 취소: DELETE /follows/follower/{userId}
    @Operation(summary = "팔로워 삭제", description = "User ID를 이용하여 팔로워를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "팔로워 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/follower/{userId}")
    public ResponseEntity<?> deleteFollower(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "팔로우 취소할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return followService.deleteFollower(userPrincipal, userId);
    }

    @Operation(summary = "나의 팔로잉 목록 조회", description = "나의 팔로잉 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.MyFollowingListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/following")
    public ResponseEntity<?> findMyFollowingList(
            @Parameter(description = "나의 팔로잉 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return followService.findMyFollowingList(page, userPrincipal);
    }

    @Operation(summary = "나의 팔로워 목록 조회", description = "나의 팔로워 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.MyFollowerListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/follower")
    public ResponseEntity<?> findMyFollowerList(
            @Parameter(description = "나의 팔로워 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return followService.findMyFollowerList(page, userPrincipal);
    }

    //- 타 유저 팔로잉 목록 조회 : GET /follows/following/{user-id}
    @Operation(summary = "타 유저 팔로잉 목록 조회", description = "타 유저의 팔로잉 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.UserFollowingListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/following/{userId}")
    public ResponseEntity<?> findUserFollowingList(
            @Parameter(description = "타 유저의 팔로잉 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "조회할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return followService.findUserFollowingList(page, userPrincipal, userId);
    }
    //- 타 유저 팔로워 목록 조회 : GET /follows/follower/{user-id}
    @Operation(summary = "타 유저 팔로워 목록 조회", description = "타 유저의 팔로워 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponseDto.UserFollowerListResWithPaging.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/follower/{userId}")
    public ResponseEntity<?> findUserFollowerList(
            @Parameter(description = "타 유저의 팔로워 목록을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page") Integer page,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "조회할 유저의 ID를 입력해주세요.", required = true) @PathVariable(value = "userId") Long userId
    ) {
        return followService.findUserFollowerList(page, userPrincipal, userId);
    }
}
