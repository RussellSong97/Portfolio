package com.cuv.domain.quickinquiry;

import com.cuv.common.YN;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.domain.quickinquiry.entity.QuickInquiry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 간편 상담 신청 서비스
 *
 * @since 2024-09-12
 * @author Joo ree Song
 * @version 1.1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuickInquiryService {

    private final QuickInquiryRepository quickInquiryRepository;

    @Transactional
    public void applyQuickInquiry(String costumerName, String costumerNumber, String costumerAsk, String entryType) {
        // DB Insert
        QuickInquiry quickInquiry = QuickInquiry.builder()
                .costumerName(costumerName)
                .costumerNumber(costumerNumber)
                .costumerAsk(costumerAsk)
                .consultationStatus(ConsultationStatus.APPLY_INQUIRY)
                .entryType(entryType)
                .agreeTermYn(YN.Y)
                .delYn(YN.N)
                .build();
        try {
            quickInquiryRepository.save(quickInquiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
