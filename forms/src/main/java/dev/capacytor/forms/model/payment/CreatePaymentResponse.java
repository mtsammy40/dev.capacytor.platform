package dev.capacytor.forms.model.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePaymentResponse {
    String reference;
    String paymentId;
    String status;
    String paymentUrl;
}
