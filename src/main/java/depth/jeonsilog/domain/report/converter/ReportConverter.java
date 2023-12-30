package depth.jeonsilog.domain.report.converter;

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
            String imageUrl = "";

            Report report = reports.get(i);
            Object target = targetList.get(i);

            if (target instanceof Review) {
                Review review = (Review) target;
                User user = review.getUser();
                name = user.getNickname();
                imageUrl = user.getProfileImg();

            } else if (target instanceof Reply) {
                Reply reply = (Reply) target;
                User user = reply.getUser();
                name = user.getNickname();
                imageUrl = user.getProfileImg();


            } else if (target instanceof Exhibition) {
                Exhibition exhibition = (Exhibition) target;
                name = exhibition.getName();
                imageUrl = exhibition.getImageUrl();

            }

            ReportResponseDto.ReportRes reportRes = ReportResponseDto.ReportRes.builder()
                    .reportId(report.getId())
                    .name(name) // 신고된 유저 혹은 전시회 이름
                    .imageUrl(imageUrl) // 신고된 유저 혹은 전시회 이미지
                    .reportType(report.getReportType())
                    .reportedId(report.getReportedId())
                    .build();

            reportResList.add(reportRes);
        }
        return reportResList;
    }

}
