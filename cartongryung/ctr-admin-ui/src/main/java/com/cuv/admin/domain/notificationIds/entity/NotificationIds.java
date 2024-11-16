package com.cuv.admin.domain.notificationIds.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
/**
 * 알림관련 아이디들
 */
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "notification_ids")
public class NotificationIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_ids_id")
    private Long id;

    private Long notificationId;

    private Long memberId;
}
