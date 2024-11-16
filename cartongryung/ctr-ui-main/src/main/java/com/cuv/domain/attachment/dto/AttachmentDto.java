package com.cuv.domain.attachment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttachmentDto {

    private Long id;

    private String uuid;

    private String uploadName;

    private String originalName;

    private String extension;

    private String path;

    @Setter
    private String realUrl;

    private Long size;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Converter
    public static class AttachmentDtoConverter implements AttributeConverter<AttachmentDto, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(AttachmentDto attribute) {
            if (attribute == null) return null;

            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        @Override
        public AttachmentDto convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;

            try {
                return objectMapper.readValue(dbData, new TypeReference<>() {});
            } catch (IOException e) {
                return null;
            }
        }

    }

    @Converter
    public static class AttachmentDtoListConverter implements AttributeConverter<List<AttachmentDto>, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<AttachmentDto> attribute) {
            if (attribute == null) return "[]";

            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                return "[]";
            }
        }

        @Override
        public List<AttachmentDto> convertToEntityAttribute(String dbData) {
            if (dbData == null) return new ArrayList<>();

            try {
                return objectMapper.readValue(dbData, new TypeReference<>() {});
            } catch (IOException e) {
                return new ArrayList<>();
            }
        }

    }

}
