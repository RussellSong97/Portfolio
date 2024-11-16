package com.cuv.domain.member.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum MemberStatus {

    NONE(null, "", 0),
    NORMAL("MST001", "정상", 1),
    PAUSE("MST002", "정지", 2),
    SECESSION("MST003", "탈퇴", 3);

    private final String code;
    private final String detail;
    private final int value;

    public static MemberStatus ofCode(String code) {
        return EnumSet.allOf(MemberStatus.class).stream()
                .filter(v -> v != MemberStatus.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(MemberStatus.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class MemberStatusConverter implements AttributeConverter<MemberStatus, String> {
        @Override
        public String convertToDatabaseColumn(MemberStatus attribute) {
            return attribute != null ? attribute.getCode() : MemberStatus.NONE.getCode();
        }

        @Override
        public MemberStatus convertToEntityAttribute(String dbData) {
            return MemberStatus.ofCode(dbData);
        }

    }

}
