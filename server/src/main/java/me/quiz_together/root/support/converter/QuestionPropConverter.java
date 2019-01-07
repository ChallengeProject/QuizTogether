package me.quiz_together.root.support.converter;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.quiz_together.root.model.question.QuestionProp;

@Converter
public class QuestionPropConverter implements AttributeConverter<QuestionProp, String> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(QuestionProp questionProp) {
        try {
            return objectMapper.writeValueAsString(questionProp);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public QuestionProp convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, QuestionProp.class);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
