package dev.capacytor.payments.provider.mpesa.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SimulatePushRequestDto(
        BigDecimal amount,
        String phoneNumber,
        String accountReference,
        String transactionDesc
) {

}
