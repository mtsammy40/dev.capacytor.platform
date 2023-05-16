package dev.capacytor.payments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {
    @Builder.Default
    private LocalDateTime received = LocalDateTime.now();
}
