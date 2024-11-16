package com.cuv.admin.domain.purchaseinquiry.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum InquiryType {

    NONE(null, "", 0),
    CONTACTABLE("INQ001", "연락 가능 문의", 1),
    WITHOUT_CONTACT("INQ002", "연락처 없는 문의", 2),
    VISIT_RESERVATION("INQ003", "방문 예약", 3);

    private final String code;
    private final String detail;
    private final int value;

    public static InquiryType ofCode(String code) {
        return EnumSet.allOf(InquiryType.class).stream()
                .filter(v -> v != InquiryType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(InquiryType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class InquiryTypeConverter implements AttributeConverter<InquiryType, String> {

        @Override
        public String convertToDatabaseColumn(InquiryType attribute) {
            return attribute != null ? attribute.getCode() : InquiryType.NONE.getCode();
        }

        @Override
        public InquiryType convertToEntityAttribute(String dbData) {
            return InquiryType.ofCode(dbData);
        }
    }
}
