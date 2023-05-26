package dev.capacytor.payments.provider.mpesa;

import dev.capacytor.payments.provider.mpesa.model.MpesaConfig;
import dev.capacytor.payments.provider.mpesa.model.SimulatePushRequestDto;
import dev.capacytor.payments.provider.mpesa.model.SimulateStkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MpesaRestClient {

    private final MpesaConfig mpesaConfig;
    private String accessToken;

    private RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(mpesaConfig.getUrl())
                .build();
    }

    protected MpesaRestClient authenticate() {
        var headers = createBasicAuthHeaders(this.mpesaConfig.getConsumerKey(), this.mpesaConfig.getConsumerSecret());
        HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
        var response = restTemplate()
                .exchange("/oauth/v1/generate?grant_type=client_credentials", HttpMethod.GET, requestEntity, Map.class);
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            this.accessToken = response.getBody().get("access_token").toString();
            return this;
        } else {
            throw new IllegalStateException("Mpesa authorization failed : statusCode " + response.getStatusCode());
        }
    }

    ResponseEntity<SimulateStkResponseDto> simulatePush(SimulatePushRequestDto request) throws MpesaApiException {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("BusinessShortCode", mpesaConfig.getShortCode());
        requestBody.put("Password", Base64
                .getEncoder()
                .encodeToString((mpesaConfig.getShortCode() + mpesaConfig.getPasskey() + timestamp).getBytes()));
        requestBody.put("Timestamp", timestamp);
        requestBody.put("TransactionType", "CustomerPayBillOnline");
        requestBody.put("Amount", request.amount());
        requestBody.put("PartyA", request.phoneNumber());
        requestBody.put("PartyB", mpesaConfig.getShortCode());
        requestBody.put("PhoneNumber", request.phoneNumber());
        requestBody.put("CallBackURL", mpesaConfig.getCallbackUrl());
        requestBody.put("AccountReference", request.accountReference());
        requestBody.put("TransactionDesc", request.transactionDesc());

        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, createAuthTokenHeaders());

        var response = restTemplate()
                .exchange("/mpesa/stkpush/v1/processrequest", HttpMethod.POST, requestEntity, SimulateStkResponseDto.class);

        if (response.getBody() == null) {
            throw new MpesaApiException("Push response is null", MpesaApiException.ErrorCode.NO_RESPONSE);
        } else {
            return response;
        }
    }

    HttpHeaders createBasicAuthHeaders(String username, String password){
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String( encodedAuth );
        var headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    HttpHeaders createAuthTokenHeaders() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.accessToken);
        return headers;
    }
}
