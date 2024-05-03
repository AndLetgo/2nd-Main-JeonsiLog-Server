package depth.jeonsilog.domain.alarm.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = 1982276713L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    public final EnumPath<AlarmType> alarmType = createEnum("alarmType", AlarmType.class);

    public final NumberPath<Long> clickId = createNumber("clickId", Long.class);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChecked = createBoolean("isChecked");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> senderId = createNumber("senderId", Long.class);

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final depth.jeonsilog.domain.user.domain.QUser user;

    public QAlarm(String variable) {
        this(Alarm.class, forVariable(variable), INITS);
    }

    public QAlarm(Path<? extends Alarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarm(PathMetadata metadata, PathInits inits) {
        this(Alarm.class, metadata, inits);
    }

    public QAlarm(Class<? extends Alarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new depth.jeonsilog.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

