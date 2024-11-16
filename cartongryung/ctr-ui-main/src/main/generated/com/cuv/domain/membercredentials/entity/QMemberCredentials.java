package com.cuv.domain.membercredentials.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberCredentials is a Querydsl query type for MemberCredentials
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCredentials extends EntityPathBase<MemberCredentials> {

    private static final long serialVersionUID = -1221905290L;

    public static final QMemberCredentials memberCredentials = new QMemberCredentials("memberCredentials");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath authNumber = createString("authNumber");

    public final StringPath authToken = createString("authToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<com.cuv.domain.membercredentials.enumset.CredentialsType> type = createEnum("type", com.cuv.domain.membercredentials.enumset.CredentialsType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> useYn = createEnum("useYn", com.cuv.common.YN.class);

    public QMemberCredentials(String variable) {
        super(MemberCredentials.class, forVariable(variable));
    }

    public QMemberCredentials(Path<? extends MemberCredentials> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberCredentials(PathMetadata metadata) {
        super(MemberCredentials.class, metadata);
    }

}

