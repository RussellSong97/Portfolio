package com.cuv.domain.exhibition.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum ExhibitionType {

    NONE(null, "", 0),
    EVENT("EXH001", "이벤트", 1),
    POPUP("EXH002", "팝업", 2),
    TOP_STRIP("EXH003", "상단 띠 배너", 3),
    MAIN_TOP("EXH004", "메인 상단 배너", 4),
    MAIN_SUB("EXH005", "메인 서브 배너", 5),
    LOGIN("EXH006", "로그인 배너", 6);

    private final String code;
    private final String detail;
    private final int value;

    public static ExhibitionType ofCode(String code) {
        return EnumSet.allOf(ExhibitionType.class).stream()
                .filter(v -> v != ExhibitionType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(ExhibitionType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Convert
    public static class ExhibitionTypeConverter implements AttributeConverter<ExhibitionType, String> {
        @Override
        public String convertToDatabaseColumn(ExhibitionType attribute) {
            return attribute != null ? attribute.getCode() : ExhibitionType.NONE.getCode();
        }

        @Override
        public ExhibitionType convertToEntityAttribute(String dbData) {
            return ExhibitionType.ofCode(dbData);
        }

    }
}
