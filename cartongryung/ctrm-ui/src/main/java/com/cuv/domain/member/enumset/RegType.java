package com.cuv.domain.member.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum RegType {

    NONE(null, "", 0),
    KAKAO("REG001", "카카오", 1),
    NAVER("REG002", "네이버", 2),
    APPLE("REG003", "애플", 3),
    GOOGLE("REG004", "구글", 4),
    EMAIL("REG005", "이메일", 5);

    private final String code;
    private final String detail;
    private final int value;

    public static RegType ofCode(String code) {
        return EnumSet.allOf(RegType.class).stream()
                .filter(v -> v != RegType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(RegType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class RegTypeConverter implements AttributeConverter<RegType, String> {
        @Override
        public String convertToDatabaseColumn(RegType attribute) {
            return attribute != null ? attribute.getCode() : RegType.NONE.getCode();
        }

        @Override
        public RegType convertToEntityAttribute(String dbData) {
            return RegType.ofCode(dbData);
        }

    }

}
