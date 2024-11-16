package com.cuv.domain.boardguide.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum BoardGuideType {

    NONE(null, "", 0),
    GUIDE("GUI001", "안내", 1),
    SELL("GUI002", "판매", 2),
    BUY("GUI003", "구매", 3);

    private final String code;
    private final String detail;
    private final int value;

    public static BoardGuideType ofCode(String code) {
        return EnumSet.allOf(BoardGuideType.class).stream()
                .filter(v -> v != BoardGuideType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(BoardGuideType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class BoardGuideTypeConverter implements AttributeConverter<BoardGuideType, String> {

        @Override
        public String convertToDatabaseColumn(BoardGuideType attribute) {
            return attribute != null ? attribute.getCode() : BoardGuideType.NONE.getCode();
        }

        @Override
        public BoardGuideType convertToEntityAttribute(String dbData) {
            return BoardGuideType.ofCode(dbData);
        }
    }
}
