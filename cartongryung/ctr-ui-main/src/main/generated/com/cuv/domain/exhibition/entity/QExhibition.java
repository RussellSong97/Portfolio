package com.cuv.domain.exhibition.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QExhibition is a Querydsl query type for Exhibition
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExhibition extends EntityPathBase<Exhibition> {

    private static final long serialVersionUID = 824957262L;

    public static final QExhibition exhibition = new QExhibition("exhibition");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final EnumPath<com.cuv.domain.exhibition.enumset.BackgroundColorType> backgroundColorType = createEnum("backgroundColorType", com.cuv.domain.exhibition.enumset.BackgroundColorType.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final DatePath<java.time.LocalDate> exhibitionEndDate = createDate("exhibitionEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> exhibitionStartDate = createDate("exhibitionStartDate", java.time.LocalDate.class);

    public final EnumPath<com.cuv.domain.exhibition.enumset.ExhibitionType> exhibitionType = createEnum("exhibitionType", com.cuv.domain.exhibition.enumset.ExhibitionType.class);

    public final NumberPath<Long> hits = createNumber("hits", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.cuv.domain.exhibition.enumset.LinkType> linkType = createEnum("linkType", com.cuv.domain.exhibition.enumset.LinkType.class);

    public final StringPath linkUrl = createString("linkUrl");

    public final SimplePath<com.cuv.domain.attachment.dto.AttachmentDto> mobileAttachment = createSimple("mobileAttachment", com.cuv.domain.attachment.dto.AttachmentDto.class);

    public final SimplePath<com.cuv.domain.attachment.dto.AttachmentDto> pcAttachment = createSimple("pcAttachment", com.cuv.domain.attachment.dto.AttachmentDto.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> viewYn = createEnum("viewYn", com.cuv.common.YN.class);

    public QExhibition(String variable) {
        super(Exhibition.class, forVariable(variable));
    }

    public QExhibition(Path<? extends Exhibition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExhibition(PathMetadata metadata) {
        super(Exhibition.class, metadata);
    }

}

