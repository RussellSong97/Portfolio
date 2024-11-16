package com.cuv.domain.linkoptioninfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkOptionInfo is a Querydsl query type for LinkOptionInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkOptionInfo extends EntityPathBase<LinkOptionInfo> {

    private static final long serialVersionUID = -1091024214L;

    public static final QLinkOptionInfo linkOptionInfo = new QLinkOptionInfo("linkOptionInfo");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

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

    public final StringPath optCtgry = createString("optCtgry");

    public final StringPath optNm = createString("optNm");

    public final EnumPath<com.cuv.common.YN> optPickType = createEnum("optPickType", com.cuv.common.YN.class);

    public final NumberPath<Integer> optType = createNumber("optType", Integer.class);

    public final StringPath pickOptDesc = createString("pickOptDesc");

    public final StringPath pickOptGrpCd = createString("pickOptGrpCd");

    public final StringPath pickOptPrice = createString("pickOptPrice");

    public final EnumPath<com.cuv.common.YN> repActiveYn = createEnum("repActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> repExposureYn = createEnum("repExposureYn", com.cuv.common.YN.class);

    public final NumberPath<Integer> salesSeCd = createNumber("salesSeCd", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QLinkOptionInfo(String variable) {
        super(LinkOptionInfo.class, forVariable(variable));
    }

    public QLinkOptionInfo(Path<? extends LinkOptionInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkOptionInfo(PathMetadata metadata) {
        super(LinkOptionInfo.class, metadata);
    }

}

