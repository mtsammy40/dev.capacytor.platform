package dev.capacytor.payments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transactions")
public class Transaction {
    @Id
    private String id;
    private BigDecimal amount;
    private String description;
    private String status;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    private String type;
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "debit_account_id")
    private String debitAccountId;
    @Column(name = "credit_account_id")
    private String creditAccountId;

    public enum TransactionType {
        PAYMENT, REFUND, PLATFORM_FEES
    }
}
