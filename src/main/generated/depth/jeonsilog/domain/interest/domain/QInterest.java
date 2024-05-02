package depth.jeonsilog.domain.interest.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterest is a Querydsl query type for Interest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterest extends EntityPathBase<Interest> {

    private static final long serialVersionUID = 433985045L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterest interest = new QInterest("interest");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final depth.jeonsilog.domain.exhibition.domain.QExhibition exhibition;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final depth.jeonsilog.domain.user.domain.QUser user;

    public QInterest(String variable) {
        this(Interest.class, forVariable(variable), INITS);
    }

    public QInterest(Path<? extends Interest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterest(PathMetadata metadata, PathInits inits) {
        this(Interest.class, metadata, inits);
    }

    public QInterest(Class<? extends Interest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exhibition = inits.isInitialized("exhibition") ? new depth.jeonsilog.domain.exhibition.domain.QExhibition(forProperty("exhibition"), inits.get("exhibition")) : null;
        this.user = inits.isInitialized("user") ? new depth.jeonsilog.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

