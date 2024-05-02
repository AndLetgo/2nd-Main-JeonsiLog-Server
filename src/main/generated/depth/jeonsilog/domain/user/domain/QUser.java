package depth.jeonsilog.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -613630505L;

    public static final QUser user = new QUser("user");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    public final ListPath<depth.jeonsilog.domain.alarm.domain.Alarm, depth.jeonsilog.domain.alarm.domain.QAlarm> alarms = this.<depth.jeonsilog.domain.alarm.domain.Alarm, depth.jeonsilog.domain.alarm.domain.QAlarm>createList("alarms", depth.jeonsilog.domain.alarm.domain.Alarm.class, depth.jeonsilog.domain.alarm.domain.QAlarm.class, PathInits.DIRECT2);

    public final ListPath<depth.jeonsilog.domain.calendar.domain.Calendar, depth.jeonsilog.domain.calendar.domain.QCalendar> calendars = this.<depth.jeonsilog.domain.calendar.domain.Calendar, depth.jeonsilog.domain.calendar.domain.QCalendar>createList("calendars", depth.jeonsilog.domain.calendar.domain.Calendar.class, depth.jeonsilog.domain.calendar.domain.QCalendar.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final ListPath<depth.jeonsilog.domain.follow.domain.Follow, depth.jeonsilog.domain.follow.domain.QFollow> followers = this.<depth.jeonsilog.domain.follow.domain.Follow, depth.jeonsilog.domain.follow.domain.QFollow>createList("followers", depth.jeonsilog.domain.follow.domain.Follow.class, depth.jeonsilog.domain.follow.domain.QFollow.class, PathInits.DIRECT2);

    public final ListPath<depth.jeonsilog.domain.follow.domain.Follow, depth.jeonsilog.domain.follow.domain.QFollow> followings = this.<depth.jeonsilog.domain.follow.domain.Follow, depth.jeonsilog.domain.follow.domain.QFollow>createList("followings", depth.jeonsilog.domain.follow.domain.Follow.class, depth.jeonsilog.domain.follow.domain.QFollow.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final BooleanPath isRecvActive = createBoolean("isRecvActive");

    public final BooleanPath isRecvExhibition = createBoolean("isRecvExhibition");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final StringPath providerId = createString("providerId");

    public final ListPath<depth.jeonsilog.domain.report.domain.Report, depth.jeonsilog.domain.report.domain.QReport> reports = this.<depth.jeonsilog.domain.report.domain.Report, depth.jeonsilog.domain.report.domain.QReport>createList("reports", depth.jeonsilog.domain.report.domain.Report.class, depth.jeonsilog.domain.report.domain.QReport.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final EnumPath<UserLevel> userLevel = createEnum("userLevel", UserLevel.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

