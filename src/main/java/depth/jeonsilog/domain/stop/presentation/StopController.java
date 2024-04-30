package depth.jeonsilog.domain.stop.presentation;

import depth.jeonsilog.domain.stop.application.StopService;
import depth.jeonsilog.domain.stop.dto.StopRequestDto;
import depth.jeonsilog.domain.stop.dto.StopResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Stop API", description = "Stop 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stop")
public class StopController {

    private final StopService stopService;

    @Operation(summary = "유저 정지", description = "유저를 정지합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정지 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "정지 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> stopUser(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser final UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 StopUserReq를 참고해주세요.", required = true) @Valid @RequestBody final StopRequestDto.StopUserReq dto
    ) {
        return stopService.stopUser(userPrincipal, dto);
    }

    @Operation(summary = "유저 정지 조회", description = "유저 정지 여부를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정지 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StopResponseDto.StopUserRes.class))}),
            @ApiResponse(responseCode = "400", description = "정지 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findUserIsStop(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser final UserPrincipal userPrincipal
    ) {
        return stopService.findUserIsStop(userPrincipal);
    }

    @Operation(summary = "정지된 유저 최초 접속", description = "정지된 유저 최초 접속 시 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "확인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "확인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping
    public ResponseEntity<?> updateIsFirstAccess(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser final UserPrincipal userPrincipal
    ) {
        return stopService.updateIsFirstAccess(userPrincipal);
    }
}
