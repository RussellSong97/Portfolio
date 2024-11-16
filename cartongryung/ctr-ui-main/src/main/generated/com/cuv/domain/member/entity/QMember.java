package com.cuv.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -756564828L;

    public static final QMember member = new QMember("member1");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final EnumPath<com.cuv.common.YN> agreeMarketingYn = createEnum("agreeMarketingYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> agreePrivacyYn = createEnum("agreePrivacyYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> agreeTermsYn = createEnum("agreeTermsYn", com.cuv.common.YN.class);

    public final EnumPath<com.cuv.common.YN> authYn = createEnum("authYn", com.cuv.common.YN.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath mobileNumber = createString("mobileNumber");

    public final StringPath password = createString("password");

    public final StringPath realName = createString("realName");

    public final EnumPath<com.cuv.domain.member.enumset.RegType> regCode = createEnum("regCode", com.cuv.domain.member.enumset.RegType.class);

    public final EnumPath<com.cuv.domain.member.enumset.MemberRole> role = createEnum("role", com.cuv.domain.member.enumset.MemberRole.class);

    public final EnumPath<com.cuv.domain.member.enumset.MemberStatus> statusCode = createEnum("statusCode", com.cuv.domain.member.enumset.MemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final DateTimePath<java.time.LocalDateTime> withdrawAt = createDateTime("withdrawAt", java.time.LocalDateTime.class);

    public final StringPath withdrawReason = createString("withdrawReason");

    public final EnumPath<com.cuv.common.YN> yearsOlderYn = createEnum("yearsOlderYn", com.cuv.common.YN.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

