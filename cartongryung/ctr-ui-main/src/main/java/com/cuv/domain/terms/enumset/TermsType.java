package com.cuv.domain.terms.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum TermsType {

    NONE(null, "", 0),
    SERVICE("TRM001", "사용자 이용약관", 1),
    PRIVACY("TRM002", "개인정보처리방침", 2);

    private final String code;
    private final String detail;
    private final int value;

    public static TermsType ofCode(String code) {
        return EnumSet.allOf(TermsType.class).stream()
                .filter(v -> v != TermsType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(TermsType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class TermsTypeConverter implements AttributeConverter<TermsType, String> {

        @Override
        public String convertToDatabaseColumn(TermsType attribute) {
            return attribute != null ? attribute.getCode() : TermsType.NONE.getCode();
        }

        @Override
        public TermsType convertToEntityAttribute(String dbData) {
            return TermsType.ofCode(dbData);
        }
    }
}
