package com.cuv.domain.boardnotice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardNotice is a Querydsl query type for BoardNotice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardNotice extends EntityPathBase<BoardNotice> {

    private static final long serialVersionUID = -207532938L;

    public static final QBoardNotice boardNotice = new QBoardNotice("boardNotice");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final ListPath<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>> attachment = this.<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>>createList("attachment", com.cuv.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> hits = createNumber("hits", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.cuv.common.YN> noticeYn = createEnum("noticeYn", com.cuv.common.YN.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> viewYn = createEnum("viewYn", com.cuv.common.YN.class);

    public QBoardNotice(String variable) {
        super(BoardNotice.class, forVariable(variable));
    }

    public QBoardNotice(Path<? extends BoardNotice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardNotice(PathMetadata metadata) {
        super(BoardNotice.class, metadata);
    }

}

