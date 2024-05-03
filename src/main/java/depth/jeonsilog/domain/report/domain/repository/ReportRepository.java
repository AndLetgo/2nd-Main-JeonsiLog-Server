package depth.jeonsilog.domain.report.domain.repository;

import depth.jeonsilog.domain.report.domain.Report;
import depth.jeonsilog.domain.report.domain.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportQuerydslRepository {

    Page<Report> findAll(Pageable pageable);

    Slice<Report> findSliceBy(Pageable pageable);

    List<Report> findByReportedIdAndReportType(Long reportedId, ReportType reportType);

    Optional<Report> findByUserIdAndReportTypeAndReportedId(Long userId, ReportType reportType, Long reportedId);
}
