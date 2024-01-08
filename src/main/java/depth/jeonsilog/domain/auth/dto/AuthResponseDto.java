package depth.jeonsilog.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

public class AuthResponseDto {

    @Data
    @Builder
    public static class AuthRes {

        @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g" , description="access token 을 출력합니다.")
        private String accessToken;

        @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.asdf8as4df865as4dfasdf65_asdfweioufsdoiuf_432jdsaFEWFSDV_sadf" , description="refresh token 을 출력합니다.")
        private String refreshToken;
    }

    @Data
    @Builder
    public static class NicknameCheckRes {

        @Schema(type = "boolean", example = "true", description = "중복일 경우 true를 출력합니다.")
        private Boolean isDuplicate;

        @Schema(type = "boolean", example = "true", description = "금칙어일 경우 true를 출력합니다.")
        private Boolean isForbidden;
    }
}
