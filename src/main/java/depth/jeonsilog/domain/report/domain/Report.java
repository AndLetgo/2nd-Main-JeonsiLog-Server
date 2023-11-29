package depth.jeonsilog.domain.report.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    // 타입별 아이디를 부여하는 것 !!
    private Long reportedId;

    @Builder
    public Report(Long id, User user, ReportType reportType, Long reportedId) {
        this.id = id;
        this.user = user;
        this.reportType = reportType;
        this.reportedId = reportedId;
    }
}
