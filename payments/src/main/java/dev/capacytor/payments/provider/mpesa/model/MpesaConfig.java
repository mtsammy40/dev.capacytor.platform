package dev.capacytor.payments.provider.mpesa.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MpesaConfig {
    private String url;
    private String shortCode;
    private String passkey;
    private String consumerKey;
    private String consumerSecret;
    private String callbackUrl;
}
