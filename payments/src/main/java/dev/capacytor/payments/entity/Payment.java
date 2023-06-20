package dev.capacytor.payments.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.capacytor.payments.entity.converter.InfoConverter;
import dev.capacytor.payments.model.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "payments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    UUID id;
    @Column(unique = true, nullable = false)
    String reference;
    @Column(name="provider_reference", unique = true, nullable = false)
    String providerReference;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = InfoConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    @Builder.Default
    Info info = new Info();
    String description;
    @Enumerated(value = EnumType.STRING)
    PaymentStatus status;
    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;
    @Column(name = "completed_at")
    LocalDateTime completedAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
    @Column(name = "originator_org_id")
    String originatorOrgId;
    @Column(name = "payment_type")
    PaymentType paymentType;
    @Column(name = "amount")
    BigDecimal amount;
    @Column(name = "currency")
    String currency;


    public enum PaymentStatus {
        PENDING, PAID, FAILED, EXPIRED, PROCESSING
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Info {
        Map<String, Object> originatorInfo;
        @Builder.Default
        MpesaInfo mpesaInfo = new MpesaInfo();
        CashInfo cashInfo = new CashInfo();
        String failureReason;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MpesaInfo {
        String phoneNumber;
        String checkoutRequestID;
        String merchantRequestID;
        String errorMessage;
        String receipt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CashInfo {
        String phoneNumber;
    }
}
