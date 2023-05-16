package dev.capacytor.payments.entity.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.HashMap;

public class MapConverter implements AttributeConverter<HashMap<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(HashMap<String, Object> attribute) {
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
    public HashMap<String, Object> convertToEntityAttribute(String dbData) {
        HashMap<String, Object> attribute = null;
        try {
            attribute = new ObjectMapper().readValue(dbData, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            attribute = new HashMap<>();
        }
        return attribute;
    }
}
