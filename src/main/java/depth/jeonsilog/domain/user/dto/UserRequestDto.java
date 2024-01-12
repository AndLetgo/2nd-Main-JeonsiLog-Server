package depth.jeonsilog.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class UserRequestDto {

    @Data
    public static class ChangeNicknameReq {

        @Schema(type = "string", example = "양파쿵야", description = "닉네임 입니다.")
        @NotBlank(message = "닉네임을 입력해야 합니다.")
        @Pattern(regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9]{2,10}", message = "한글, 영어, 숫자 가능, 2~10자, 특수기호 불가")
        private String nickname;
    }

    @Data
    public static class UpdateFcmToken {

        @Schema(type = "string", example = "c8z22dyWSxqH_e7Gk..", description = "Fcm Token 입니다.")
        private String fcmToken;
    }
}
