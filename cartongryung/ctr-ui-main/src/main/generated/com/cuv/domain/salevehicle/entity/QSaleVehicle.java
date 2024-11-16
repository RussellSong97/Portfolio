package com.cuv.domain.salevehicle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSaleVehicle is a Querydsl query type for SaleVehicle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaleVehicle extends EntityPathBase<SaleVehicle> {

    private static final long serialVersionUID = -1070842666L;

    public static final QSaleVehicle saleVehicle = new QSaleVehicle("saleVehicle");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath carPlateNumber = createString("carPlateNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> linkInfoId = createNumber("linkInfoId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath ownerName = createString("ownerName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QSaleVehicle(String variable) {
        super(SaleVehicle.class, forVariable(variable));
    }

    public QSaleVehicle(Path<? extends SaleVehicle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaleVehicle(PathMetadata metadata) {
        super(SaleVehicle.class, metadata);
    }

}

