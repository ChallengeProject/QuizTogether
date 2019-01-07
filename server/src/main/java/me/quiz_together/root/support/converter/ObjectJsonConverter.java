package me.quiz_together.root.support.converter;

import static java.util.Objects.isNull;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//TODO : generic
//@Converter
public class ObjectJsonConverter<T extends Object> implements AttributeConverter<T, String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    private Class<T> type;

    public ObjectJsonConverter(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public String convertToDatabaseColumn(T t) {
        try {
            if (isNull(t)) {
                return null;
            }
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("convertToDatabaseColumn error");
            throw new RuntimeException("convertToDatabaseColumn error");
        }
    }

    @Override
    public T convertToEntityAttribute(String s) {
        try {
            if (StringUtils.isBlank(s)) {
                return null;
            }
            return OBJECT_MAPPER.readValue(s, type);
        } catch (IOException e) {
            log.error("convertToEntityAttribute");
            throw new RuntimeException("convertToEntityAttribute");
        }
    }
}
