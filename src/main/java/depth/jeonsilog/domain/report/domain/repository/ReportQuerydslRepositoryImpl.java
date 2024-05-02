package depth.jeonsilog.domain.report.domain.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import depth.jeonsilog.domain.exhibition.domain.QExhibition;
import depth.jeonsilog.domain.reply.domain.QReply;
import depth.jeonsilog.domain.report.domain.QReport;
import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.review.domain.QReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ReportQuerydslRepositoryImpl implements ReportQuerydslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Report> findReportsWithSortingAndCounting(Pageable pageable) {
        QReport report = QReport.report;
        QReply reply = QReply.reply;
        QReview review = QReview.review;
        QExhibition exhibition = QExhibition.exhibition;

        List<Report> content = queryFactory
                .selectFrom(report)
                .leftJoin(reply).on(reply.id.eq(report.reportedId))
                .leftJoin(review).on(review.id.eq(reply.review.id))
                .leftJoin(exhibition).on(exhibition.id.eq(report.reportedId))
                .where(report.id.in(
                        JPAExpressions.select(report.id.min())
                                .from(report)
                                .groupBy(report.reportType, report.reportedId, report.isChecked)
                ))
                .orderBy(report.isChecked.asc(), report.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(report)
                .where(report.id.in(
                        JPAExpressions.select(report.id.min())
                                .from(report)
                                .groupBy(report.reportType, report.reportedId, report.isChecked)
                ))
                .fetch()
                .size();

        return new PageImpl<>(content, pageable, total);
    }
}
