package dev.capacytor.payments.provider.mpesa.model;

import dev.capacytor.payments.model.PaymentResult;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class MpesaPaymentResult extends PaymentResult {
    private String reference;
    private String resultCode;
    private String resultDesc;
    private String trxReceipt;

    public boolean isSuccess() {
        return "0".equals(resultCode);
    }
}
