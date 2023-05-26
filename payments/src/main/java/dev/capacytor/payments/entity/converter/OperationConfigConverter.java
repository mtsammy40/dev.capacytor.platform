package dev.capacytor.payments.entity.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.capacytor.payments.entity.MethodConfig;
import jakarta.persistence.AttributeConverter;

public class OperationConfigConverter implements AttributeConverter<MethodConfig.OperationConfig, String> {
    @Override
    public String convertToDatabaseColumn(MethodConfig.OperationConfig attribute) {
        String attributeJson = null;
        try {
            attributeJson = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            attributeJson = "{}";
        }
        return attributeJson;
    }

    @Override
    public MethodConfig.OperationConfig convertToEntityAttribute(String dbData) {
        MethodConfig.OperationConfig attribute = null;
        try {
            attribute = new ObjectMapper().readValue(dbData, MethodConfig.OperationConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            attribute = new MethodConfig.OperationConfig();
        }
        return attribute;
    }
}
