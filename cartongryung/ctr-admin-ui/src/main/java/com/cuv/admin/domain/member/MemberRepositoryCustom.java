package com.cuv.admin.domain.member;

import com.cuv.admin.domain.member.dto.*;
import com.cuv.admin.domain.notificatonMember.dto.NotificatonMemberMemberIdAndTokenDto;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MemberRepositoryCustom {
    Page<MemberListDto> searchAllMember(MemberSearchDto condition, PageRequest request);

    MemberDetailDto searchMemberById(Long id);

    Page<MemberListDto> searchAllMemberWithdrawList(MemberSearchDto condition, PageRequest request);

    MemberWithdrawDto searchMemberWithdrawPopup(Long id);

    List<MemberPurchaseInquiryListDto> searchMemberPurchaseList(Long id);

    List<MemberSaleInquiryListDto> searchMemberSaleList(Long id);

    List<MemberJoinCountDto> searchAllWeekJoinMemberCountByMain(String week);

    Long searchAllTodayJoinMemberCountByMain(String today);

    Long searchAllMemberCountByMain();

    List<MemberListDto> adminActiveMemberTableExcel(MemberSearchDto condition);

    List<MemberListDto> adminWithdrawMemberTableExcel(MemberSearchDto condition);

    // 개별회원
    List<NotificatonMemberMemberIdAndTokenDto> findAllMemberIdAndFcmToken(List<Long> ids);
    
    // 모든 맴버 
    List<NotificatonMemberMemberIdAndTokenDto> findMemberIdAndFcmToken();

    List<NotificatonMemberMemberIdAndTokenDto> findAllMarketingMemberIdAndFcmToken();

    Page<MemberListDto> searchGetFcmTokenMember(MemberSearchDto condition, PageRequest request);

    Page<MemberListDto> searchGetFcmTokenMarketingMember(MemberSearchDto condition, PageRequest request);

}
