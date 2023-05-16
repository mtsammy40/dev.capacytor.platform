package dev.capacytor.payments.entity.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class ClassConverter implements AttributeConverter<Object, String > {
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        String attributeJson = null;
        try {
            attributeJson = new ObjectMapper().writeValueAsString(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            attributeJson = "{}";
        }
        return attributeJson;
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        Object attribute = null;
        try {
            attribute = new ObjectMapper().readValue(dbData, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            attribute = new Object();
        }
        return attribute;
    }
}
