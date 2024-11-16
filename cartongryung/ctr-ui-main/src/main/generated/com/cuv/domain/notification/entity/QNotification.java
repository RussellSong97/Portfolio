package com.cuv.domain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -1394868986L;

    public static final QNotification notification = new QNotification("notification");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final ListPath<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>> attachment = this.<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>>createList("attachment", com.cuv.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath failReason = createString("failReason");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath linkUrl = createString("linkUrl");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath pushStatus = createString("pushStatus");

    public final EnumPath<com.cuv.common.YN> readYn = createEnum("readYn", com.cuv.common.YN.class);

    public final StringPath sendStatus = createString("sendStatus");

    public final StringPath target = createString("target");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QNotification(String variable) {
        super(Notification.class, forVariable(variable));
    }

    public QNotification(Path<? extends Notification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotification(PathMetadata metadata) {
        super(Notification.class, metadata);
    }

}

