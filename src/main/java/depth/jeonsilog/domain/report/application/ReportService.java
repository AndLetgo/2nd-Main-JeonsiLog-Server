package depth.jeonsilog.domain.report.application;

import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.reply.application.ReplyService;
import depth.jeonsilog.domain.reply.domain.repository.ReplyRepository;
import depth.jeonsilog.domain.report.converter.ReportConverter;
import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.report.domain.ReportType;
import depth.jeonsilog.domain.report.domain.repository.ReportRepository;
import depth.jeonsilog.domain.report.dto.ReportRequestDto;
import depth.jeonsilog.domain.report.dto.ReportResponseDto;
import depth.jeonsilog.domain.review.application.ReviewService;
import depth.jeonsilog.domain.review.domain.repository.ReviewRepository;
import depth.jeonsilog.domain.user.application.UserService;
import depth.jeonsilog.domain.user.domain.Role;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.global.DefaultAssert;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import depth.jeonsilog.global.payload.ApiResponse;
import depth.jeonsilog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final ExhibitionRepository exhibitionRepository;

    private final UserService userService;
    private final ReviewService reviewService;
    private final ReplyService replyService;
    private final ExhibitionService exhibitionService;

    // TODO Think : Report의 reportedId는 타입 별 해당 PK(ID)임 !

    // Description : 신고
    @Transactional
    public ResponseEntity<?> report(UserPrincipal userPrincipal, ReportRequestDto.ReportReq reportReq) {

        User user = userService.validateUserByToken(userPrincipal);
        Long id = reportReq.getReportedId();
        ReportType reportType = reportReq.getReportType();

        validate(reportType, id);

        Report report = ReportConverter.toReport(user, id, reportType);
        reportRepository.save(report);

        ApiResponse apiResponse = ApiResponse.toApiResponse(Message.builder().message("신고가 완료되었습니다.").build());
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 신고 목록 조회
    public ResponseEntity<?> findReportList(Integer page, UserPrincipal userPrincipal) {

        User user = userService.validateUserByToken(userPrincipal);
        DefaultAssert.isTrue(user.getRole().equals(Role.ADMIN), "관리자만 조회할 수 있습니다.");

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "createdDate");
        Page<Report> reportPage = reportRepository.findAll(pageRequest);
        List<Report> reports = reportPage.getContent();

        DefaultAssert.isTrue(!reports.isEmpty(), "신고 내역이 존재하지 않습니다.");

        List<Object> targetList = createTargetList(reports);

        List<ReportResponseDto.ReportRes> reportResList = ReportConverter.toReportResList(reports, targetList);

        ApiResponse apiResponse = ApiResponse.toApiResponse(reportResList);
        return ResponseEntity.ok(apiResponse);

    }

    private void validate(ReportType reportType, Long id) {
        switch (reportType) {
            case REVIEW -> reviewService.validateReviewById(id);
            case REPLY -> replyService.validateReplyById(id);
            case EXHIBITION -> {
                Exhibition exhibition = exhibitionService.validateExhibitionById(id);
                DefaultAssert.isTrue(exhibition.getImageUrl() == null, "이미 등록된 포스터가 존재합니다.");
            }
        }
    }

    private List<Object> createTargetList(List<Report> reports) {
        List<Object> targetList = new ArrayList<>();

        for (Report report : reports) {
            Object target = new Object();

            switch (report.getReportType()) {
                case REVIEW -> target = reviewService.validateReviewById(report.getReportedId());
                case REPLY -> target = replyService.validateReplyById(report.getReportedId());
                case EXHIBITION -> target = exhibitionService.validateExhibitionById(report.getReportedId());
            }
            targetList.add(target);
        }
        return targetList;
    }
}
