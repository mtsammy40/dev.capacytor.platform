package dev.capacytor.payments.provider.mpesa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SimulateStkResponseDto(
    @JsonProperty("MerchantRequestID")
    String merchantRequestID,
    @JsonProperty("CheckoutRequestID")
    String checkoutRequestID,
    @JsonProperty("ResponseCode")
    String responseCode,
    @JsonProperty("ResponseDescription")
    String responseDescription,
    @JsonProperty("CustomerMessage")
    String customerMessage) {

    public boolean isSuccess() {
        return responseCode.equals("0");
    }
}
