package com.cuv.domain.linkinfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkInfo is a Querydsl query type for LinkInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkInfo extends EntityPathBase<LinkInfo> {

    private static final long serialVersionUID = 1484979104L;

    public static final QLinkInfo linkInfo = new QLinkInfo("linkInfo");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final NumberPath<Long> brandNbr = createNumber("brandNbr", Long.class);

    public final StringPath brandNm = createString("brandNm");

    public final NumberPath<Long> carClassNbr = createNumber("carClassNbr", Long.class);

    public final StringPath carClassNm = createString("carClassNm");

    public final NumberPath<Long> carGradeNbr = createNumber("carGradeNbr", Long.class);

    public final StringPath carGradeNm = createString("carGradeNm");

    public final StringPath carInfoJson = createString("carInfoJson");

    public final StringPath colorNm = createString("colorNm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath engineForm = createString("engineForm");

    public final StringPath enginesize = createString("enginesize");

    public final StringPath extShape = createString("extShape");

    public final StringPath frstRegistDe = createString("frstRegistDe");

    public final StringPath fuel = createString("fuel");

    public final StringPath gradeFuelRate = createString("gradeFuelRate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath insptValidPdDe = createString("insptValidPdDe");

    public final StringPath istdTrans = createString("istdTrans");

    public final StringPath person = createString("person");

    public final StringPath prye = createString("prye");

    public final NumberPath<Long> repCarClassNbr = createNumber("repCarClassNbr", Long.class);

    public final StringPath repCarClassNm = createString("repCarClassNm");

    public final StringPath tireSizeBack = createString("tireSizeBack");

    public final StringPath tireSizeFront = createString("tireSizeFront");

    public final NumberPath<Long> trvlDstnc = createNumber("trvlDstnc", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final StringPath vhrno = createString("vhrno");

    public final StringPath vin = createString("vin");

    public final StringPath yearType = createString("yearType");

    public QLinkInfo(String variable) {
        super(LinkInfo.class, forVariable(variable));
    }

    public QLinkInfo(Path<? extends LinkInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkInfo(PathMetadata metadata) {
        super(LinkInfo.class, metadata);
    }

}

