package depth.jeonsilog.domain.exhibition.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExhibition is a Querydsl query type for Exhibition
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExhibition extends EntityPathBase<Exhibition> {

    private static final long serialVersionUID = 347430943L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExhibition exhibition = new QExhibition("exhibition");

    public final depth.jeonsilog.domain.common.QBaseEntity _super = new depth.jeonsilog.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Integer> exhibitionSeq = createNumber("exhibitionSeq", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath information = createString("information");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final EnumPath<OperatingKeyword> operatingKeyword = createEnum("operatingKeyword", OperatingKeyword.class);

    public final depth.jeonsilog.domain.place.domain.QPlace place;

    public final StringPath price = createString("price");

    public final EnumPath<PriceKeyword> priceKeyword = createEnum("priceKeyword", PriceKeyword.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public final StringPath startDate = createString("startDate");

    //inherited
    public final EnumPath<depth.jeonsilog.domain.common.Status> status = _super.status;

    public QExhibition(String variable) {
        this(Exhibition.class, forVariable(variable), INITS);
    }

    public QExhibition(Path<? extends Exhibition> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExhibition(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExhibition(PathMetadata metadata, PathInits inits) {
        this(Exhibition.class, metadata, inits);
    }

    public QExhibition(Class<? extends Exhibition> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new depth.jeonsilog.domain.place.domain.QPlace(forProperty("place")) : null;
    }

}

