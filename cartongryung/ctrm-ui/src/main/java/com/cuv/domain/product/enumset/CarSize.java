package com.cuv.domain.product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum CarSize {

    NONE(null, "", "", 0),
    LIGHT("CAS001", "경형", "A", 1),
    SMALL("CAS002", "소형", "B", 2),
    SEMIMEDIUM("CAS003", "준중형", "C", 3),
    MEDIUM("CAS004", "중형", "D", 4),
    SEMILARGE("CAS005", "준대형", "E", 5),
    LARGE("CAS006", "대형", "F", 6),
    OTHER("CAS007", "기타(차급)", "OTHER", 7);

    private final String code;
    private final String name;
    private final String engName;
    private final int value;

    public static CarSize ofCode(String code) {
        return EnumSet.allOf(CarSize.class).stream()
                .filter(v -> v != CarSize.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(CarSize.NONE);
    }

    public static CarSize ofEngName(String engName) {
        return EnumSet.allOf(CarSize.class).stream()
                .filter(v -> v != CarSize.NONE && v.getEngName().equals(engName))
                .findAny()
                .orElse(CarSize.NONE);
    }

    @Override
    public String toString() { return this.getCode();}

    @Converter
    public static class CarSizeConverter implements AttributeConverter<CarSize, String> {

        @Override
        public String convertToDatabaseColumn(CarSize attribute) {
            return attribute != null ? attribute.getCode() : CarSize.NONE.getCode();
        }

        @Override
        public CarSize convertToEntityAttribute(String dbData) {
            return CarSize.ofCode(dbData);
        }
    }

}
