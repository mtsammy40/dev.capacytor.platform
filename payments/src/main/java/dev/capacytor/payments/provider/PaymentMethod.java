package dev.capacytor.payments.provider;

import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayData;


public interface PaymentMethod {
    <P extends PayData> PaymentMethod prepare(Payment payment, P payData);
    void initiate();
    void processResults(Payment payment, JsonNode paymentResult);
}
