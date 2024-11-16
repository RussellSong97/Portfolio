package com.cuv.domain.linkhistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkHistory is a Querydsl query type for LinkHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkHistory extends EntityPathBase<LinkHistory> {

    private static final long serialVersionUID = 935504566L;

    public static final QLinkHistory linkHistory = new QLinkHistory("linkHistory");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath businessUseHistory = createString("businessUseHistory");

    public final StringPath carBodyShape = createString("carBodyShape");

    public final StringPath carDisplacement = createString("carDisplacement");

    public final StringPath carDistanceDrive = createString("carDistanceDrive");

    public final StringPath carName = createString("carName");

    public final StringPath carNameDetail = createString("carNameDetail");

    public final StringPath carOption = createString("carOption");

    public final StringPath carYear = createString("carYear");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final StringPath data = createString("data");

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath firstInsuranceDate = createString("firstInsuranceDate");

    public final StringPath floodLossCount = createString("floodLossCount");

    public final StringPath fuelType = createString("fuelType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath noJoinDate = createString("noJoinDate");

    public final StringPath otherCarDamageAccidentCount = createString("otherCarDamageAccidentCount");

    public final StringPath otherCarDamageInsuranceSum = createString("otherCarDamageInsuranceSum");

    public final StringPath ownerChangeCount = createString("ownerChangeCount");

    public final StringPath ownerChangeType = createString("ownerChangeType");

    public final StringPath personalUseHistory = createString("personalUseHistory");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath rentalUseHistory = createString("rentalUseHistory");

    public final StringPath selfDamageAccidentCount = createString("selfDamageAccidentCount");

    public final StringPath selfDamageInsuranceSum = createString("selfDamageInsuranceSum");

    public final StringPath standardDate = createString("standardDate");

    public final StringPath theftLossCount = createString("theftLossCount");

    public final StringPath theftLossDate = createString("theftLossDate");

    public final StringPath totalLossCount = createString("totalLossCount");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final StringPath useType = createString("useType");

    public QLinkHistory(String variable) {
        super(LinkHistory.class, forVariable(variable));
    }

    public QLinkHistory(Path<? extends LinkHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkHistory(PathMetadata metadata) {
        super(LinkHistory.class, metadata);
    }

}

