package dev.capacytor.payments.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.capacytor.payments.entity.converter.ClassConverter;
import dev.capacytor.payments.model.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "payments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    @Id
    @Column(unique = true, nullable = false)
    String id;
    @Column(unique = true, nullable = false)
    String reference;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = ClassConverter.class)
    Info info;
    String description;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Info {
        @Builder.Default
        HashMap<String, Objects> originatorInfo = new HashMap<>();
        @JsonIgnore
        @Builder.Default
        MpesaInfo mpesaInfo = new MpesaInfo();
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
}
