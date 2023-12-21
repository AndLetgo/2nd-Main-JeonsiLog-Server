package depth.jeonsilog.domain.interest.application;

import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.interest.converter.InterestConverter;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.repository.InterestRepository;
import depth.jeonsilog.domain.interest.dto.InterestResponseDto;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.domain.repository.UserRepository;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;

    // 즐겨찾기 등록
    @Transactional
    public ResponseEntity<?> registerInterest(UserPrincipal userPrincipal, Long exhibitionId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");
        Exhibition findExhibition = exhibition.get();

        Optional<Interest> checkDuplication = interestRepository.findByUserIdAndExhibitionId(findUser.getId(), exhibition.get().getId());
        DefaultAssert.isTrue(checkDuplication.isEmpty(), "즐겨찾기가 이미 존재합니다.");

        Interest interest = InterestConverter.toInterest(findUser, findExhibition);
        interestRepository.save(interest);

        InterestResponseDto.InterestRes interestRes = InterestConverter.toInterestRes(interest);
        ApiResponse apiResponse = ApiResponse.toApiResponse(interestRes);

        return ResponseEntity.ok(apiResponse);
    }

    // 즐겨찾기 해제
    @Transactional
    public ResponseEntity<?> deleteInterest(UserPrincipal userPrincipal, Long exhibitionId) {
        User findUser = userService.validateUserByToken(userPrincipal);
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        DefaultAssert.isTrue(exhibition.isPresent(), "전시회 id가 올바르지 않습니다.");

        Optional<Interest> interest = interestRepository.findByUserIdAndExhibitionId(findUser.getId(), exhibition.get().getId());
        Interest findInterest = interest.get();
        interestRepository.delete(findInterest);

        ApiResponse apiResponse = ApiResponse.toApiResponse(
                Message.builder().message("[" + exhibition.get().getName() + "]를 즐겨찾기 해제했습니다.").build());

        return ResponseEntity.ok(apiResponse);
    }

    // 즐겨찾기 목록 조회
    public ResponseEntity<?> getInterestList(UserPrincipal userPrincipal) {
        User findUser = userService.validateUserByToken(userPrincipal);
        List<Interest> findInterest = interestRepository.findAllByUserId(findUser.getId());

        List<InterestResponseDto.InterestListRes> interestListResList = InterestConverter.toInterestListRes(findInterest);

        ApiResponse apiResponse = ApiResponse.toApiResponse(interestListResList);

        return ResponseEntity.ok(apiResponse);
    }
}
