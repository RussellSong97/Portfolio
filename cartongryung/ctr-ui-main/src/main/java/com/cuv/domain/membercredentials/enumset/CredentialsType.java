package com.cuv.domain.membercredentials.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum CredentialsType {

    NONE(null, "", 0),
    JOIN("CR001", "회원가입", 1),
    SNS("CR002", "SNS", 2),
    INFO("CR003", "정보수정", 3),
    VISIT("CR004", "방문예약", 4);

    private final String code;
    private String name;
    private final int value;

    public static CredentialsType ofCode(String code) {
        return EnumSet.allOf(CredentialsType.class).stream()
                .filter(v -> v != CredentialsType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(CredentialsType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class CredentialsTypeConverter implements AttributeConverter<CredentialsType, String> {

        @Override
        public String convertToDatabaseColumn(CredentialsType attribute) {
            return attribute != null ? attribute.getCode() : CredentialsType.NONE.getCode();
        }

        @Override
        public CredentialsType convertToEntityAttribute(String dbData) {
            return CredentialsType.ofCode(dbData);
        }
    }
}
