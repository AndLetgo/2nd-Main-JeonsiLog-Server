package depth.jeonsilog.domain.report.converter;

import depth.jeonsilog.domain.common.Status;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.report.domain.ReportType;
import depth.jeonsilog.domain.report.dto.ReportResponseDto;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ReportConverter {

    // Make Report
    public static Report toReport(User user, Long id, ReportType reportType) {
        return Report.builder()
                .user(user)
                .reportType(reportType)
                .reportedId(id)
                .isChecked(false)
                .build();
    }

    // ReportList & TargetList -> ReportResList
    public static List<ReportResponseDto.ReportRes> toReportResList(List<Report> reports, List<Object> targetList) {

        List<ReportResponseDto.ReportRes> reportResList = new ArrayList<>();
        for (int i = 0; i < reports.size(); i++) {

            String name = "";
            Report report = reports.get(i);
            Object target = targetList.get(i);
            Long clickId = null;

            if (target instanceof Review) {
                Review review = (Review) target;
                if (review.getStatus().equals(Status.DELETE))
                    continue;
                User user = review.getUser();
                name = user.getNickname();
                clickId = review.getId();

            } else if (target instanceof Reply) {
                Reply reply = (Reply) target;
                if (reply.getStatus().equals(Status.DELETE))
                    continue;
                User user = reply.getUser();
                name = user.getNickname();
                clickId = reply.getReview().getId();

            } else {
                Exhibition exhibition = (Exhibition) target;
                name = exhibition.getName();
                clickId = exhibition.getId();
            }
            ReportResponseDto.ReportRes reportRes = ReportResponseDto.ReportRes.builder()
                    .name(name) // 신고된 유저 혹은 전시회 이름
                    .reportType(report.getReportType())
                    .reportedId(report.getReportedId())
                    .clickId(clickId)
                    .isChecked(report.getIsChecked())
                    .counting(report.getCounting())
                    .build();

            reportResList.add(reportRes);
        }
        return reportResList;
    }

    public static ReportResponseDto.ReportResListWithPaging toReportResListWithPaging(boolean hasNextPage, List<ReportResponseDto.ReportRes> reportResList) {
        return ReportResponseDto.ReportResListWithPaging.builder()
                .hasNextPage(hasNextPage)
                .data(reportResList)
                .build();
    }
}
