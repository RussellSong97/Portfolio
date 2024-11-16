package com.cuv.admin.domain.calendar;

import com.cuv.admin.domain.calendar.dto.*;
import com.cuv.admin.domain.calendar.dto.consult.ConsultDto;
import com.cuv.admin.domain.calendar.dto.counslorDealer.CounselorDealerDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatMonthAndYearDto;
import com.cuv.admin.domain.calendar.entity.Calendar;

import java.util.List;

public interface CalendarRepositoryCustom {

    List<MemberStatDto> searchMemberStat(StatSearchDto condition);

    List<MemberStatMonthAndYearDto> searchMemberStatMonth(StatSearchDto condition);

    List<MemberStatMonthAndYearDto> searchMemberStatYear(StatSearchDto condition);

    List<CounselorDealerDto> searchCounselorForSale(StatSearchDto condition);

    List<Calendar> searchCalendar(StatSearchDto condition);

    List<CounselorDealerDto> searchCounselorForPurchase(StatSearchDto condition);

    List<CounselorDealerDto> searchDealerForPurchase(StatSearchDto condition);

    List<CounselorDealerDto> searchDealerForSale(StatSearchDto condition);

    List<ConsultDto> searchConsultForPurchase(StatSearchDto condition);

    List<ConsultDto> searchConsultForSale(StatSearchDto condition);

    List<MemberStatDto> searchMemberStatExcel(StatSearchDto condition);

    List<MemberStatMonthAndYearDto> searchMemberStatMonthExcel(StatSearchDto condition);

    List<MemberStatMonthAndYearDto> searchMemberStatYearExcel(StatSearchDto condition);

}
