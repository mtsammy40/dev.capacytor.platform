package dev.capacytor.forms.entity.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentData {
    private String paymentId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Amount paymentAmount;
    private Currency paymentCurrency;

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    public enum PaymentMethod {
        MOBILE_MONEY,
        CASH
    }

}