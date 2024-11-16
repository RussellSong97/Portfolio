package com.cuv.domain.linkcode.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLinkCode is a Querydsl query type for LinkCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkCode extends EntityPathBase<LinkCode> {

    private static final long serialVersionUID = 144257054L;

    public static final QLinkCode linkCode = new QLinkCode("linkCode");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> afterServiceDate = createDate("afterServiceDate", java.time.LocalDate.class);

    public final SimplePath<com.cuv.domain.attachment.dto.AttachmentDto> attachment = createSimple("attachment", com.cuv.domain.attachment.dto.AttachmentDto.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath linkDataNm = createString("linkDataNm");

    public final NumberPath<Long> parentLinkNbrId = createNumber("parentLinkNbrId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final NumberPath<Integer> viewOrder = createNumber("viewOrder", Integer.class);

    public final EnumPath<com.cuv.common.YN> viewYn = createEnum("viewYn", com.cuv.common.YN.class);

    public QLinkCode(String variable) {
        super(LinkCode.class, forVariable(variable));
    }

    public QLinkCode(Path<? extends LinkCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkCode(PathMetadata metadata) {
        super(LinkCode.class, metadata);
    }

}

