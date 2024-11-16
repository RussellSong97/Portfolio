package com.cuv.domain.notificationMember.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 알림
 */
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "notification_member")
public class NotificationMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationMemberId;

    private Long memberId;

    private Long notificationId;

    private String failReason;

    @Column(name="read_yn")
    private YN readYn;

}
