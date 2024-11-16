package com.cuv.domain.boardfaq.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardFaq is a Querydsl query type for BoardFaq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFaq extends EntityPathBase<BoardFaq> {

    private static final long serialVersionUID = -1328003856L;

    public static final QBoardFaq boardFaq = new QBoardFaq("boardFaq");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

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

    public QBoardFaq(String variable) {
        super(BoardFaq.class, forVariable(variable));
    }

    public QBoardFaq(Path<? extends BoardFaq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardFaq(PathMetadata metadata) {
        super(BoardFaq.class, metadata);
    }

}

