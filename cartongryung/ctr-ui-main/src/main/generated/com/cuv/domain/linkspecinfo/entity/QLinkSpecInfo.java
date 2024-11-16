package com.cuv.domain.linkspecinfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkSpecInfo is a Querydsl query type for LinkSpecInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkSpecInfo extends EntityPathBase<LinkSpecInfo> {

    private static final long serialVersionUID = -664317642L;

    public static final QLinkSpecInfo linkSpecInfo = new QLinkSpecInfo("linkSpecInfo");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath alphanumCtgry = createString("alphanumCtgry");

    public final EnumPath<com.cuv.common.YN> brandActiveYn = createEnum("brandActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> brandExposureYn = createEnum("brandExposureYn", com.cuv.common.YN.class);

    public final NumberPath<Long> carGradeNbr = createNumber("carGradeNbr", Long.class);

    public final EnumPath<com.cuv.common.YN> classActiveYn = createEnum("classActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> classExposureYn = createEnum("classExposureYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> cmtYn = createEnum("cmtYn", com.cuv.common.YN.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final EnumPath<com.cuv.common.YN> gradeActiveYn = createEnum("gradeActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> gradeExposureYn = createEnum("gradeExposureYn", com.cuv.common.YN.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdateDttm = createDateTime("lastUpdateDttm", java.time.LocalDateTime.class);

    public final EnumPath<com.cuv.common.YN> repActiveYn = createEnum("repActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> repExposureYn = createEnum("repExposureYn", com.cuv.common.YN.class);

    public final NumberPath<Integer> salesSeCd = createNumber("salesSeCd", Integer.class);

    public final StringPath specCtgry = createString("specCtgry");

    public final StringPath specImageId = createString("specImageId");

    public final StringPath specMultiItem = createString("specMultiItem");

    public final StringPath specNm = createString("specNm");

    public final StringPath specUom = createString("specUom");

    public final StringPath specValue = createString("specValue");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QLinkSpecInfo(String variable) {
        super(LinkSpecInfo.class, forVariable(variable));
    }

    public QLinkSpecInfo(Path<? extends LinkSpecInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkSpecInfo(PathMetadata metadata) {
        super(LinkSpecInfo.class, metadata);
    }

}

