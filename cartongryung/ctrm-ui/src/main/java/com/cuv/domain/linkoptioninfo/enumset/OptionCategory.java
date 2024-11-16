package com.cuv.domain.linkoptioninfo.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum OptionCategory {

    NONE(null, "", 0),
    INTERNAL("21", "내장", 1),
    SAFETY_PERFORMANCE("31", "안전/성능", 2),
    EXTERNAL("61", "외장", 3),
    CONVENIENCE_FUNCTION("81", "편의/기능", 4);

    private final String code;
    private final String detail;
    private final int value;

    public static OptionCategory ofCode(String code) {
        return EnumSet.allOf(OptionCategory.class).stream()
                .filter(v -> v != OptionCategory.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(OptionCategory.NONE);
    }

    // 카이즈유: 숫자 코드 -> 지정된 상세로 전환
    public static String getDetailByCode(String code) {
        return EnumSet.allOf(OptionCategory.class).stream()
                .filter(v -> v != OptionCategory.NONE && v.getCode().equals(code))
                .map(OptionCategory::getDetail)
                .findAny()
                .orElse(OptionCategory.NONE.getDetail());
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class OptionCategoryConverter implements AttributeConverter<OptionCategory, String> {

        @Override
        public String convertToDatabaseColumn(OptionCategory attribute) {
            return attribute != null ? attribute.getCode() : OptionCategory.NONE.getCode();
        }

        @Override
        public OptionCategory convertToEntityAttribute(String dbData) {
            return OptionCategory.ofCode(dbData);
        }
    }
}
