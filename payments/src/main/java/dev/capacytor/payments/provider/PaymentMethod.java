package dev.capacytor.payments.provider;

import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayRequest;


public interface PaymentMethod {
    PaymentMethod prepare(Payment payment, PayRequest payData);
    void initiate();
    void processResults(Payment payment, JsonNode paymentResult);
}
