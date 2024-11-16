package com.cuv.domain.purchaseinquiry.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum ConsultationType {

    NONE(null, "", 0),
    CHATTING("CSL001", "채팅", 1),
    CALL("CSL002", "전화", 2),
    VISIT("CSL003", "방문예약", 3);

    private final String code;
    private final String detail;
    private final int value;

    public static ConsultationType ofCode(String code) {
        return EnumSet.allOf(ConsultationType.class).stream()
                .filter(v -> v != ConsultationType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(ConsultationType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class ConsultationTypeConverter implements AttributeConverter<ConsultationType, String> {

        @Override
        public String convertToDatabaseColumn(ConsultationType attribute) {
            return attribute != null ? attribute.getCode() : ConsultationType.NONE.getCode();
        }

        @Override
        public ConsultationType convertToEntityAttribute(String dbData) {
            return ConsultationType.ofCode(dbData);
        }
    }
}
