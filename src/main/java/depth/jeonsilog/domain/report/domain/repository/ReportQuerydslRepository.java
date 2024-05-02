package depth.jeonsilog.domain.report.domain.repository;

import depth.jeonsilog.domain.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReportQuerydslRepository {

    Page<Report> findReportsWithSortingAndCounting(Pageable pageable);
}
