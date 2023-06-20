package dev.capacytor.payments.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.capacytor.payments.entity.converter.InfoConverter;
import dev.capacytor.payments.entity.converter.PaymentConfigurationConverter;
import dev.capacytor.payments.model.Currency;
import dev.capacytor.payments.model.PaymentType;
import dev.capacytor.payments.provider.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "clients")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    UUID id;
    @Enumerated(value = EnumType.STRING)
    Status status;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = PaymentConfigurationConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    @Builder.Default
    PaymentConfiguration paymentConfiguration = new PaymentConfiguration();

    public enum Status {
        ACTIVE, INACTIVE
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class PaymentConfiguration {
        @Builder.Default
        List<Method> allowedMethods = new ArrayList<>();
        @Builder.Default
        List<Currency> allowedCurrencies = new ArrayList<>();
        LimitConfiguration limitConfiguration;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Method {
        boolean isDefault = false;
        PaymentType paymentType;
        LimitConfiguration limitConfiguration;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class LimitConfiguration {
        BigDecimal minTrxAmount;
        BigDecimal maxTrxAmount;
        BigDecimal maxDailyAmount;
    }
}
