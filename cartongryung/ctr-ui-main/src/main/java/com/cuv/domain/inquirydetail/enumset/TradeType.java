package com.cuv.domain.inquirydetail.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum TradeType {

    NONE(null, "", 0),
    BUY("TRD001", "구매", 1),
    SELL("TRD002", "판매", 2);

    private final String code;
    private final String detail;
    private final int value;

    public static TradeType ofCode(String code) {
        return EnumSet.allOf(TradeType.class).stream()
                .filter(v -> v != TradeType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(TradeType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class TradeTypeConverter implements AttributeConverter<TradeType, String> {

        @Override
        public String convertToDatabaseColumn(TradeType attribute) {
            return attribute != null ? attribute.getCode() : TradeType.NONE.getCode();
        }

        @Override
        public TradeType convertToEntityAttribute(String dbData) {
            return TradeType.ofCode(dbData);
        }
    }
}
