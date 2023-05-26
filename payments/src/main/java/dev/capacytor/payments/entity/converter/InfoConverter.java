package dev.capacytor.payments.entity.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.capacytor.payments.entity.Payment;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class InfoConverter implements AttributeConverter<Payment.Info, String> {
    @Override
    public String convertToDatabaseColumn(Payment.Info attribute) {
        String attributeJson = "{}";
        try {
            attributeJson = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(attribute);
        } catch (Exception e) {
            log.error("Unable to serialize Payment.Info to dbString", e);
        }
        return attributeJson;
    }

    @Override
    public Payment.Info convertToEntityAttribute(String dbData) {
        Payment.Info attribute = new Payment.Info();
        try {
            if(StringUtils.hasText(dbData)) {
                attribute = new ObjectMapper().readValue(dbData, Payment.Info.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unable to deserialize Payment.Info from dbValue {}", dbData);
        }
        return attribute;
    }
}
