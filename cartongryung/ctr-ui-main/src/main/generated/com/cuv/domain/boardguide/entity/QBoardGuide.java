package com.cuv.domain.boardguide.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardGuide is a Querydsl query type for BoardGuide
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardGuide extends EntityPathBase<BoardGuide> {

    private static final long serialVersionUID = -1576320644L;

    public static final QBoardGuide boardGuide = new QBoardGuide("boardGuide");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final SimplePath<com.cuv.domain.attachment.dto.AttachmentDto> attachment = createSimple("attachment", com.cuv.domain.attachment.dto.AttachmentDto.class);

    public final EnumPath<com.cuv.domain.boardguide.enumset.BoardGuideType> boardGuideType = createEnum("boardGuideType", com.cuv.domain.boardguide.enumset.BoardGuideType.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> viewYn = createEnum("viewYn", com.cuv.common.YN.class);

    public QBoardGuide(String variable) {
        super(BoardGuide.class, forVariable(variable));
    }

    public QBoardGuide(Path<? extends BoardGuide> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardGuide(PathMetadata metadata) {
        super(BoardGuide.class, metadata);
    }

}

