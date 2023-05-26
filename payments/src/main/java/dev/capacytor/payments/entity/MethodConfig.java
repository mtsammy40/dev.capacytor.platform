package dev.capacytor.payments.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.capacytor.payments.entity.converter.MapConverter;
import dev.capacytor.payments.entity.converter.OperationConfigConverter;
import dev.capacytor.payments.model.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "methods")
public class MethodConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    Status status;
    @Enumerated(EnumType.STRING)
    PaymentType type;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = MapConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    HashMap<String, Object> integrationConfig;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = OperationConfigConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    OperationConfig operationConfig;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class OperationConfig  {
        BigDecimal maxTrxAmount;
        BigDecimal minTrxAmount;
        BigDecimal maxDailyTrxAmount;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}
