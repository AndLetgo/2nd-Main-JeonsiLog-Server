package depth.jeonsilog.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class AuthRequestDto {

    @Data
    public static class SignUpReq {

        @Schema( type = "string", example = "123123", description="카카오 고유 유저 ID 입니다.")
        private String providerId;

        @Schema( type = "string", example = "string", description="카카오톡 닉네임 입니다.")
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

        @Schema( type = "string", example = "string", description="계정 비밀번호 입니다.")
        private String providerId;
    }

    @Data
    public static class RefreshTokenReq {

        @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g", description="refresh token 입니다." )
        private String refreshToken;
    }

    @Data
    public static class ChangePasswordReq {

        @Schema( type = "string", example = "string", description="기존 비밀번호 입니다.")
        @NotBlank
        @NotNull
        private String oldPassword;

        @Schema( type = "string", example = "string123", description="신규 비밀번호 입니다.")
        @NotBlank
        @NotNull
        private String newPassword;

        @Schema( type = "string", example = "string123", description="신규 비밀번호 확인란 입니다.")
        @NotBlank
        @NotNull
        private String reNewPassword;
    }
}
