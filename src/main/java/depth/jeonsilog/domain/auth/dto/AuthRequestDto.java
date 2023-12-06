package depth.jeonsilog.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class AuthRequestDto {

    @Data
    public static class SignUpReq {

        @Schema( type = "string", example = "123123", description="카카오 고유 유저 ID 입니다.")
        private String providerId;

        @Schema( type = "string", example = "string", description="닉네임 입니다.")
        @NotBlank(message = "닉네임을 입력해야 합니다.")
        @Pattern(regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9]{2,10}", message = "한글, 영어, 숫자 가능, 2~10자, 특수기호 불가")
        private String nickname;

        @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
        @Email
        private String email;

        @Schema( type = "string", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", description="프로필 사진 URL 입니다.")
        private String profileImgUrl;
    }

    @Data
    public static class SignInReq {

        @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
        @Email
        private String email;

        @Schema( type = "string", example = "string", description="providerId 입니다.")
        @NotBlank(message = "providerId를 입력해야합니다.")
        private String providerId;
    }

    @Data
    public static class RefreshTokenReq {

        @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g", description="refresh token 입니다." )
        @NotBlank(message = "refreshTokend을 입력해야합니다.")
        private String refreshToken;
    }
}
