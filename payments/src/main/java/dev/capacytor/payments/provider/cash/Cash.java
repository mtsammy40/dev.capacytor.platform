package dev.capacytor.payments.provider.cash;

import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayData;
import dev.capacytor.payments.provider.PaymentMethod;
import dev.capacytor.payments.provider.cash.model.CashPayData;

public class Cash implements PaymentMethod {
    private Payment payment;
    @Override
    public <P extends PayData> PaymentMethod prepare(Payment payment, P payData) {
        if (payData instanceof CashPayData cashPayData) {
            var cashInfo = payment.getInfo().getCashInfo() == null ? new Payment.CashInfo() : payment.getInfo().getCashInfo();
            cashInfo.setPhoneNumber(cashPayData.getPhoneNumber());
            payment.getInfo().setCashInfo(cashInfo);
            this.payment = payment;
        } else {
            throw new IllegalArgumentException("Invalid pay data type");
        }
        return this;
    }

    @Override
    public void initiate() {
        this.processResults(payment, null);
    }

    @Override
    public void processResults(Payment payment, JsonNode paymentResult) {
        this.payment.setStatus(Payment.PaymentStatus.PAID);
    }
}
