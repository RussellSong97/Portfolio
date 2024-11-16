package com.cuv.admin.domain.exhibition.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum LinkType {

    NONE(null, "", "",0),
    SELF("LNK001", "_self", "현재 창", 1),
    BLANK("LNK002", "_blank", "새 창", 2),
    EMPTY("LNK003", "", "링크없음", 3);

    private final String code;
    private final String target;
    private final String detail;
    private final int value;

    public static LinkType ofCode(String code) {
        return EnumSet.allOf(LinkType.class).stream()
                .filter(v -> v != LinkType.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(LinkType.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Convert
    public static class LinkTypeConverter implements AttributeConverter<LinkType, String> {
        @Override
        public String convertToDatabaseColumn(LinkType attribute) {
            return attribute != null ? attribute.getCode() : LinkType.NONE.getCode();
        }

        @Override
        public LinkType convertToEntityAttribute(String dbData) {
            return LinkType.ofCode(dbData);
        }

    }
}
