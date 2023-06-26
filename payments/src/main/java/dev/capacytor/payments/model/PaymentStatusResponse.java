package dev.capacytor.payments.model;

import dev.capacytor.payments.entity.Payment;

public record PaymentStatusResponse(PaymentType type, Payment.PaymentStatus status, String reference) {
}
