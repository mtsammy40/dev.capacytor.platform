package dev.capacytor.payments.model;


import java.math.BigDecimal;

public record CreatePaymentRequest(String reference,
                                   String description,
                                   String currency,
                                   BigDecimal amount,
                                   PaymentType paymentType) {
}
