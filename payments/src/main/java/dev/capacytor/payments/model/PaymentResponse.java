package dev.capacytor.payments.model;


import dev.capacytor.payments.entity.Payment;

public record PaymentResponse(String reference, Payment.PaymentStatus status, String paymentId) {
}
