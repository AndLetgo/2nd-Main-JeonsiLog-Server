package depth.jeonsilog.domain.report.domain.repository;

import depth.jeonsilog.domain.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportQuerydslRepository {

    Page<Report> findAll(Pageable pageable);

    Slice<Report> findSliceBy(Pageable pageable);
}
