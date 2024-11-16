package com.cuv.domain.exhibition.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum BackgroundColorType {

    NONE(null, "", "", 0),
    BLUE("COL001", "파란색", "color01", 1),
    NAVY("COL002", "남색", "color02", 2),
    YELLOW("COL003", "노란색", "color03", 3),
    RED("COL004", "빨간색", "color04", 4);

    private final String code;
    private final String detail;
    private final String className;
    private final int value;

    public static BackgroundColorType ofCode(String code) {
        return EnumSet.allOf(BackgroundColorType.class).stream()
                .filter(v -> v != BackgroundColorType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(BackgroundColorType.NONE);
    }


    @Override
    public String toString() {
        return this.getCode();
    }

    @Convert
    public static class BackgroundColorTypeConverter implements AttributeConverter<BackgroundColorType, String> {
        @Override
        public String convertToDatabaseColumn(BackgroundColorType attribute) {
            return attribute != null ? attribute.getCode() : BackgroundColorType.NONE.getCode();
        }

        @Override
        public BackgroundColorType convertToEntityAttribute(String dbData) {
            return BackgroundColorType.ofCode(dbData);
        }

    }
}
