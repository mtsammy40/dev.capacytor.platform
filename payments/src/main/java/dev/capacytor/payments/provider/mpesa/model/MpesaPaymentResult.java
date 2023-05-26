package dev.capacytor.payments.provider.mpesa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.model.PaymentResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MpesaPaymentResult extends PaymentResult {
    private String providerReference;
    private String resultCode;
    private String resultDesc;
    private String trxReceipt;
    private String phoneNumber;

    public boolean isSuccess() {
        return "0".equals(resultCode);
    }

    public MpesaPaymentResult(JsonNode rawData) {
        super(rawData);
        if(!rawData.has("Body")) {
            throw new IllegalArgumentException("Invalid Mpesa callback");
        }
        try {
            this.providerReference = rawData.at("/Body/stkCallback/CheckoutRequestID").asText();
            this.resultCode = rawData.at("/Body/stkCallback/ResultCode").asText();
            this.resultDesc = rawData.at("/Body/stkCallback/ResultDesc").asText();
            var items = rawData.at("/Body/stkCallback/CallbackMetadata").withArray("/Item");
            if(items.isArray()) {
                for(JsonNode node : items) {
                    String key = node.path("Name").asText();
                    switch (key) {
                        case "MpesaReceiptNumber" -> this.trxReceipt = node.path("Value").asText();
                        case "PhoneNumber" -> this.phoneNumber = node.path("Value").asText();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid callback");
        }
    }
}
