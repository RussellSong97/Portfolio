package com.cuv.admin.domain.member.dto;

import com.cuv.admin.domain.member.enumset.MemberStatus;
import com.cuv.admin.domain.member.enumset.RegType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "MemberListDto",
        description = "회원 정보 목록 출력"
)
public class MemberListDto {
    private Long id;
    private String email;
    private String realName;
    private String mobileNumber;
    private Long buyCarCount;
    private Long buyCarPriceCount;
    private MemberStatus statusCode;
    private RegType regCode;
    private LocalDateTime memberCreatedAt;
    private LocalDateTime lastLoginAt;
    private LocalDateTime withdrawAt;
    private String withdrawReason;
    private Long purchaseCount;
    private Long saleCount;

    public String getMobileNumber() {
        if (mobileNumber != null && mobileNumber.length() == 11) {
            return mobileNumber.substring(0, 3) + "-" + mobileNumber.substring(3, 7) + "-" + mobileNumber.substring(7, 11);
        }
        return mobileNumber;
    }
}
