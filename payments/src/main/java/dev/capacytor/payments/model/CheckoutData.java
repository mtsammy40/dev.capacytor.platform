package dev.capacytor.payments.model;

import dev.capacytor.payments.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public final class CheckoutData {
    private String paymentId;
    private String paymentType;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String returnUrl;
    private String status;
    private MpesaData mpesaData;
    private Payment.OrderInfo orderInfo;

    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Data
    public static class MpesaData {
        private String phoneNumber;
    }


}
