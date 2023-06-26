package dev.capacytor.payments.model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public final class PayRequest {
    private String paymentId;
    private String phoneNumber;
}
