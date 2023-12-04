package depth.jeonsilog.domain.auth.converter;

import depth.jeonsilog.domain.auth.domain.Token;
import depth.jeonsilog.domain.auth.dto.AuthRequestDto;
import depth.jeonsilog.domain.auth.dto.AuthResponseDto;
import depth.jeonsilog.domain.auth.dto.TokenMapping;
import depth.jeonsilog.domain.user.domain.Provider;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthConverter {

    // DTO -> USER
    public static User toUser(AuthRequestDto.SignUpReq signUpReq, PasswordEncoder passwordEncoder) {

        return User.builder()
                .providerId(signUpReq.getProviderId())
                .nickname(signUpReq.getNickname())
                .email(signUpReq.getEmail())
                .password(passwordEncoder.encode(signUpReq.getProviderId()))
                .profileImg(signUpReq.getProfileImgUrl())
                .provider(Provider.KAKAO)
                .role(Role.USER)
                .isOpen(true)
                .isRecvFollowing(true)
                .isRecvActive(true)
                .build();
    }

    // DTO -> TOKEN
    public static Token toToken(TokenMapping tokenMapping) {

        return Token.builder()
                .userEmail(tokenMapping.getUserEmail())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();
    }

    // TOKEN -> DTO
    public static AuthResponseDto.AuthRes toAuthRes(Token token, TokenMapping tokenMapping) {

        return AuthResponseDto.AuthRes.builder()
                .accessToken(tokenMapping.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
