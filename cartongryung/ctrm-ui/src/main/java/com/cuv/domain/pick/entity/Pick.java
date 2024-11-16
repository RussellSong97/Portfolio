package com.cuv.domain.pick.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 찜
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "pick")
public class Pick extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id")
    private Long id;

    @Column(name = "member_id")
    @Comment("회원 일련번호")
    private Long memberId;

    @Column(name = "product_id")
    @Comment("상품 일련번호")
    private Long productId;

    @Column(name="read_yn")
    @Comment("읽음 여부")
    private YN readYn;

}
