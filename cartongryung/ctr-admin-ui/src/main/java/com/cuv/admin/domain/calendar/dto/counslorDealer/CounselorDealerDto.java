package com.cuv.admin.domain.calendar.dto.counslorDealer;

import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "CounselorDealerDto",
        description = "통계를 낼 때 불러오는 상담사, 딜러의 정보"
)
public class CounselorDealerDto {
    /**
     * memberAdmin
     */
    @Column(name = "member_admin_id")
    private Long id;

    private String realName;

    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;

    /**
     * inquiryDetail
     */

    private Long inquiryDetailId;

    // 구매 판매 여부
    private TradeType tradeTypeCode;

    // 상담 상태
    private ConsultationStatus consultationStatus;

    private String createdAt;

    // 상담 완료
    private Long completedCounsel;

    // 거래 완료
    private Long completedTrade;

    @Column(name = "date")
    private LocalDate date;

    // 지우지 말 것
    public CounselorDealerDto getCounselorStat(Long counselorId) {
        if (this.id.equals(counselorId)) {
            return this;
        }
        return null;
    }

    // 지우지 말 것
    public Long getCompletedCounsel() {
        return this.completedCounsel;
    }

    public Long getCompletedTrade() {
        return this.completedTrade;
    }

    public String changeLocalDateToString(LocalDate date) {
        return date.toString();
    }

}
