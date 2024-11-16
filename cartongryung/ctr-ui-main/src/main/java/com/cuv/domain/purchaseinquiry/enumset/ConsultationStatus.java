package com.cuv.domain.purchaseinquiry.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum ConsultationStatus {

    NONE(null, "", 0),
    APPLICATION_COMPLETE("COS001", "상담접수완료", 1),
    RESERVATION_COMPLETE("COS002", "방문예약완료", 2),
    CONSULTATION_COMPLETE("COS003", "상담완료", 3),
    PREPARING_PAYMENT("COS004", "결제준비중", 4),
    NOTIFY_APPROVAL("COS005", "승인결과안내", 5),
    PAYMENT_COMPLETE("COS006", "결제완료", 6),
    ACQUISITION_COMPLETE("COS007", "인수완료", 7),
    DISAPPROVED("COS008", "승인불가", 8),
    CANCEL_RESERVATION("COS009", "방문예약취소", 9),
    PREPARING_DOCUMENT("COS010", "서류준비중", 10),
    TRANSACTION_COMPLETE("COS011", "거래완료", 11),
    CANCEL_SALE("COS012", "판매취소", 12);

    private final String code;
    private final String detail;
    private final int value;

    public static ConsultationStatus ofCode(String code) {
        return EnumSet.allOf(ConsultationStatus.class).stream()
                .filter(v -> v != ConsultationStatus.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(ConsultationStatus.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class ConsultationStatusConverter implements AttributeConverter<ConsultationStatus, String> {

        @Override
        public String convertToDatabaseColumn(ConsultationStatus attribute) {
            return attribute != null ? attribute.getCode() : ConsultationStatus.NONE.getCode();
        }

        @Override
        public ConsultationStatus convertToEntityAttribute(String dbData) {
            return ConsultationStatus.ofCode(dbData);
        }
    }
}
