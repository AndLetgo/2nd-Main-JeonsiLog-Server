package depth.jeonsilog.domain.stop.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStop is a Querydsl query type for Stop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStop extends EntityPathBase<Stop> {

    private static final long serialVersionUID = 1741118405L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStop stop = new QStop("stop");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isFirstAccess = createBoolean("isFirstAccess");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath reason = createString("reason");

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final depth.jeonsilog.domain.user.domain.QUser user;

    public QStop(String variable) {
        this(Stop.class, forVariable(variable), INITS);
    }

    public QStop(Path<? extends Stop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStop(PathMetadata metadata, PathInits inits) {
        this(Stop.class, metadata, inits);
    }

    public QStop(Class<? extends Stop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new depth.jeonsilog.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

