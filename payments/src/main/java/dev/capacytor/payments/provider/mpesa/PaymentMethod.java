package dev.capacytor.payments.provider.mpesa;

import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayData;


public interface PaymentMethod {
    <P extends PayData> void prepare(Payment payment, P payData);
    void pay(Payment payment);

    void processResults(Payment payment, JsonNode paymentResult);
}
