package com.cuv.domain.memberadmin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberAdmin is a Querydsl query type for MemberAdmin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAdmin extends EntityPathBase<MemberAdmin> {

    private static final long serialVersionUID = 72240854L;

    public static final QMemberAdmin memberAdmin = new QMemberAdmin("memberAdmin");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath employeeNumber = createString("employeeNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath intro = createString("intro");

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath mobileNumber = createString("mobileNumber");

    public final StringPath password = createString("password");

    public final SimplePath<com.cuv.domain.attachment.dto.AttachmentDto> profileImageJson = createSimple("profileImageJson", com.cuv.domain.attachment.dto.AttachmentDto.class);

    public final StringPath realName = createString("realName");

    public final EnumPath<com.cuv.domain.member.enumset.MemberRole> role = createEnum("role", com.cuv.domain.member.enumset.MemberRole.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> useYn = createEnum("useYn", com.cuv.common.YN.class);

    public QMemberAdmin(String variable) {
        super(MemberAdmin.class, forVariable(variable));
    }

    public QMemberAdmin(Path<? extends MemberAdmin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberAdmin(PathMetadata metadata) {
        super(MemberAdmin.class, metadata);
    }

}

