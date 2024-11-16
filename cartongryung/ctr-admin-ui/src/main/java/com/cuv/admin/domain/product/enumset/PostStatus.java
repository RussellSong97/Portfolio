package com.cuv.admin.domain.product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum PostStatus {

    NONE(null, "", 0),
    WAITING("PST001", "대기", 1),
    POST("PST002", "게시", 2),
    POSTSTOP("PST003", "게시 중지", 3),
    SOLDOUT("PST004", "판매 완료", 4);

    private final String code;
    private final String detail;
    private final int value;

    public static PostStatus ofCode(String code) {
        return EnumSet.allOf(PostStatus.class).stream()
                .filter(v -> v != PostStatus.NONE && v.getCode().equals(code))
                .findAny()
                .orElse(PostStatus.NONE);
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    @Converter
    public static class PostStatusConverter implements AttributeConverter<PostStatus, String> {

        @Override
        public String convertToDatabaseColumn(PostStatus attribute) {
            return attribute != null ? attribute.getCode() : PostStatus.NONE.getCode();
        }

        @Override
        public PostStatus convertToEntityAttribute(String dbData) {
            return PostStatus.ofCode(dbData);
        }
    }
}
