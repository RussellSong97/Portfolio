package com.cuv.domain.wishlist.entity;

import com.cuv.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "wishlist")
public class Wishlist extends BaseEntity {

    /** 구매 희망 차량 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    /** 구매 문의 일련번호 */
    private Long purchaseInquiryId;

    /** 상품 일련번호 */
    private Long productId;

}
