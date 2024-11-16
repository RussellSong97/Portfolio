package com.cuv.domain.linkvehicleinfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkVehicleInfo is a Querydsl query type for LinkVehicleInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkVehicleInfo extends EntityPathBase<LinkVehicleInfo> {

    private static final long serialVersionUID = -53288810L;

    public static final QLinkVehicleInfo linkVehicleInfo = new QLinkVehicleInfo("linkVehicleInfo");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final EnumPath<com.cuv.common.YN> brandActiveYn = createEnum("brandActiveYn", com.cuv.common.YN.class);

    public final StringPath brandEngNm = createString("brandEngNm");

    public final EnumPath<com.cuv.common.YN> brandExposureYn = createEnum("brandExposureYn", com.cuv.common.YN.class);

    public final StringPath brandImageUrl = createString("brandImageUrl");

    public final NumberPath<Long> brandNbr = createNumber("brandNbr", Long.class);

    public final StringPath brandNm = createString("brandNm");

    public final StringPath brandUrl = createString("brandUrl");

    public final NumberPath<Integer> carAsortCode = createNumber("carAsortCode", Integer.class);

    public final StringPath carClassEngNm = createString("carClassEngNm");

    public final NumberPath<Long> carClassNbr = createNumber("carClassNbr", Long.class);

    public final StringPath carClassNm = createString("carClassNm");

    public final NumberPath<Long> carGradeNbr = createNumber("carGradeNbr", Long.class);

    public final StringPath carGradeNm = createString("carGradeNm");

    public final StringPath carGradeNmEng = createString("carGradeNmEng");

    public final StringPath carRivalNbr = createString("carRivalNbr");

    public final StringPath carSize = createString("carSize");

    public final StringPath ciuCode = createString("ciuCode");

    public final EnumPath<com.cuv.common.YN> classActiveYn = createEnum("classActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> classExposureYn = createEnum("classExposureYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> cmtYn = createEnum("cmtYn", com.cuv.common.YN.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath discontinuedDt = createString("discontinuedDt");

    public final StringPath extShape = createString("extShape");

    public final EnumPath<com.cuv.common.YN> gradeActiveYn = createEnum("gradeActiveYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> gradeExposureYn = createEnum("gradeExposureYn", com.cuv.common.YN.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.cuv.common.YN> importYn = createEnum("importYn", com.cuv.common.YN.class);

    public final StringPath istdTrans = createString("istdTrans");

    public final DateTimePath<java.time.LocalDateTime> lastUpdateDttm = createDateTime("lastUpdateDttm", java.time.LocalDateTime.class);

    public final StringPath prodNationCd = createString("prodNationCd");

    public final EnumPath<com.cuv.common.YN> recommYn = createEnum("recommYn", com.cuv.common.YN.class);

    public final NumberPath<Integer> releaseDt = createNumber("releaseDt", Integer.class);

    public final EnumPath<com.cuv.common.YN> repActiveYn = createEnum("repActiveYn", com.cuv.common.YN.class);

    public final NumberPath<Long> repCarClassNbr = createNumber("repCarClassNbr", Long.class);

    public final StringPath repCarClassNm = createString("repCarClassNm");

    public final EnumPath<com.cuv.common.YN> repExposureYn = createEnum("repExposureYn", com.cuv.common.YN.class);

    public final StringPath repImageUrl = createString("repImageUrl");

    public final NumberPath<Integer> salePrice = createNumber("salePrice", Integer.class);

    public final NumberPath<Integer> salePriceUnit = createNumber("salePriceUnit", Integer.class);

    public final NumberPath<Integer> salesSeCd = createNumber("salesSeCd", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final NumberPath<Integer> useType = createNumber("useType", Integer.class);

    public final StringPath vehicleInfoJson = createString("vehicleInfoJson");

    public final StringPath yearType = createString("yearType");

    public QLinkVehicleInfo(String variable) {
        super(LinkVehicleInfo.class, forVariable(variable));
    }

    public QLinkVehicleInfo(Path<? extends LinkVehicleInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkVehicleInfo(PathMetadata metadata) {
        super(LinkVehicleInfo.class, metadata);
    }

}

