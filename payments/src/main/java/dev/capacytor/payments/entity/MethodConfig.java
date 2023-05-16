package dev.capacytor.payments.entity;

import dev.capacytor.payments.entity.converter.ClassConverter;
import dev.capacytor.payments.entity.converter.MapConverter;
import dev.capacytor.payments.model.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "methods")
public class MethodConfig {
    @Id
    String id;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    Status status;
    @Enumerated(EnumType.STRING)
    PaymentType type;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = MapConverter.class)
    HashMap<String, Object> integrationConfig;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = ClassConverter.class)
    OperationConfig operationConfig;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static class OperationConfig {
        String maxTrxAmount;
        String minTrxAmount;
        String maxDailyTrxAmount;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}
