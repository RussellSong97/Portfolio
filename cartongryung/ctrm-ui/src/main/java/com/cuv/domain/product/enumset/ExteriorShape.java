package com.cuv.domain.product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum ExteriorShape {

    NONE(null, "", "", 0),
    BUS("EXT001", "버스", "BUS", 1),
    CONVERTIBLE("EXT002", "컨버터블", "CONVERTIBLE", 2),
    COUPE("EXT003", "쿠페", "COUPE", 3),
    HATCHBACK("EXT004", "해치백", "HATCHBACK", 4),
    LIMOUSINE("EXT005", "리무진", "LIMOUSINE", 5),
    MINIBUS("EXT006", "미니버스", "MINIBUS", 6),
    PICKUPTRUCK("EXT007", "픽업트럭", "PICKUPTRUCK", 7),
    RV("EXT008", "RV", "RV", 8),
    SEDAN("EXT009", "세단", "SEDAN", 9),
    SUV("EXT010", "SUV", "SUV", 10),
    TRUCK("EXT011", "트럭", "TRUCK", 11),
    WAGON("EXT012", "왜건", "WAGON", 12),
    OTHER("EXT013", "기타(외형)", "OTHER", 13);

    private final String code;
    private final String name;
    private final String engName;
    private final int value;

    public static ExteriorShape ofCode(String code) {
        return EnumSet.allOf(ExteriorShape.class).stream()
                .filter(v -> v != ExteriorShape.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(ExteriorShape.NONE);
    }

    public static ExteriorShape ofEngName(String engName) {
        return EnumSet.allOf(ExteriorShape.class).stream()
                .filter(v -> v != ExteriorShape.NONE && v.getEngName().equals(engName))
                .findAny()
                .orElse(ExteriorShape.NONE);
    }

    @Override
    public String toString() { return this.getCode();}

    @Converter
    public static class ExteriorShapeConverter implements AttributeConverter<ExteriorShape, String> {

        @Override
        public String convertToDatabaseColumn(ExteriorShape attribute) {
            return attribute != null ? attribute.getCode() : ExteriorShape.NONE.getCode();
        }

        @Override
        public ExteriorShape convertToEntityAttribute(String dbData) {
            return ExteriorShape.ofCode(dbData);
        }
    }

}
