package depth.jeonsilog.domain.auth.application;

import depth.jeonsilog.domain.auth.converter.AuthConverter;
import depth.jeonsilog.domain.auth.domain.Token;
import depth.jeonsilog.domain.auth.domain.repository.TokenRepository;
import depth.jeonsilog.domain.auth.dto.*;
import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final CustomTokenProviderService customTokenProviderService;
    private final UserService userService;

    @Transactional
    public ResponseEntity<?> signUp(AuthRequestDto.SignUpReq signUpReq){

        // 이메일은 unique가 아님.. 이메일로 찾는 이유 ?
        DefaultAssert.isTrue(!userRepository.existsByEmailAndStatus(signUpReq.getEmail(), Status.ACTIVE), "해당 이메일로 가입한 유저가 존재합니다.");

        User user = AuthConverter.toUser(signUpReq, passwordEncoder);
        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("회원가입에 성공하였습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> signIn(AuthRequestDto.SignInReq signInReq){

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInReq.getEmail(),
                signInReq.getProviderId()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = AuthConverter.toToken(tokenMapping);

        tokenRepository.save(token);

        AuthResponseDto.AuthRes authResponse = AuthConverter.toAuthRes(token, tokenMapping);

        ApiResponse apiResponse = ApiResponse.toApiResponse(authResponse);
        
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> refresh(AuthRequestDto.RefreshTokenReq tokenRefreshRequest){
        // 1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        // refresh token 정보 값을 업데이트 한다.
        // 시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponseDto.AuthRes authResponse = AuthConverter.toAuthRes(updateToken, tokenMapping);

        ApiResponse apiResponse = ApiResponse.toApiResponse(authResponse);

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> signout(UserPrincipal userPrincipal) {

        Optional<Token> token = tokenRepository.findByUserEmail(userPrincipal.getEmail());
        DefaultAssert.isTrue(token.isPresent(), "이미 로그아웃 되었습니다");

        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("로그아웃 되었습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> checkNickname(String nickname) {

        AuthResponseDto.NicknameCheckRes nicknameCheckRes = AuthResponseDto.NicknameCheckRes.builder()
                .isAvailable(!userRepository.existsByNickname(nickname))
                .build();

        ApiResponse apiResponse = ApiResponse.toApiResponse(nicknameCheckRes);

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken){

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }
}
