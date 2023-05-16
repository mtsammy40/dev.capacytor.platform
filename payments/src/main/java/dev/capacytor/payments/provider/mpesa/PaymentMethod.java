package dev.capacytor.payments.provider.mpesa;

import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayData;
import dev.capacytor.payments.model.PaymentResult;

import java.util.Map;
import java.util.Objects;

public interface PaymentMethod {
    <P extends PayData> void prepare(Payment payment, P payData);
    void pay(Payment payment);

    void processResults(Payment payment, Map<String, Object> paymentResult);
}
