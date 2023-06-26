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
import java.util.ArrayList;
import java.util.List;
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
    @Column(name="provider_reference", unique = true)
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
    UUID originatorOrgId;
    @Column(name = "payment_type")
    @Enumerated(value = EnumType.STRING)
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
        @Builder.Default
        OrderInfo orderInfo = new OrderInfo();
        @Builder.Default
        MpesaInfo mpesaInfo = new MpesaInfo();
        CashInfo cashInfo;
        String failureReason;
    }

    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Data
    public static class OrderInfo {
        @Builder.Default
        private String discountPercentage = "0";
        @Builder.Default
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private List<Order> orders = new ArrayList<>();
        private String customerName;
        private String customerEmail;
        private String customerPhoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Order {
        String itemDescription;
        String quantity;
        BigDecimal unitPrice;
        BigDecimal totalPrice;
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
