package depth.jeonsilog.domain.rating.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRating is a Querydsl query type for Rating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRating extends EntityPathBase<Rating> {

    private static final long serialVersionUID = 898374075L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRating rating = new QRating("rating");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final depth.jeonsilog.domain.exhibition.domain.QExhibition exhibition;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public final depth.jeonsilog.domain.user.domain.QUser user;

    public QRating(String variable) {
        this(Rating.class, forVariable(variable), INITS);
    }

    public QRating(Path<? extends Rating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRating(PathMetadata metadata, PathInits inits) {
        this(Rating.class, metadata, inits);
    }

    public QRating(Class<? extends Rating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exhibition = inits.isInitialized("exhibition") ? new depth.jeonsilog.domain.exhibition.domain.QExhibition(forProperty("exhibition"), inits.get("exhibition")) : null;
        this.user = inits.isInitialized("user") ? new depth.jeonsilog.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

