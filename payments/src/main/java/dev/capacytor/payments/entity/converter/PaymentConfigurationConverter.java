package dev.capacytor.payments.entity.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.capacytor.payments.entity.Client;
import dev.capacytor.payments.entity.Payment;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class PaymentConfigurationConverter implements AttributeConverter<Client.PaymentConfiguration, String> {
    @Override
    public String convertToDatabaseColumn(Client.PaymentConfiguration attribute) {
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
    public Client.PaymentConfiguration convertToEntityAttribute(String dbData) {
        Client.PaymentConfiguration attribute = new Client.PaymentConfiguration();
        try {
            if(StringUtils.hasText(dbData)) {
                attribute = new ObjectMapper().readValue(dbData, Client.PaymentConfiguration.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unable to deserialize Payment.Info from dbValue {}", dbData);
        }
        return attribute;
    }
}
