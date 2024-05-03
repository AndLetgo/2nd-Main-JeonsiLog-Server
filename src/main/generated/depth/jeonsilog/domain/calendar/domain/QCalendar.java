package depth.jeonsilog.domain.calendar.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCalendar is a Querydsl query type for Calendar
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalendar extends EntityPathBase<Calendar> {

    private static final long serialVersionUID = 750392893L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCalendar calendar = new QCalendar("calendar");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    public final StringPath caption = createString("caption");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final DatePath<java.time.LocalDate> photoDate = createDate("photoDate", java.time.LocalDate.class);

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final depth.jeonsilog.domain.user.domain.QUser user;

    public QCalendar(String variable) {
        this(Calendar.class, forVariable(variable), INITS);
    }

    public QCalendar(Path<? extends Calendar> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCalendar(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCalendar(PathMetadata metadata, PathInits inits) {
        this(Calendar.class, metadata, inits);
    }

    public QCalendar(Class<? extends Calendar> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new depth.jeonsilog.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

