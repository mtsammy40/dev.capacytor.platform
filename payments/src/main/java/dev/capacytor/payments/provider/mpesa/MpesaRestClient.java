package dev.capacytor.payments.provider.mpesa;

import dev.capacytor.payments.provider.mpesa.model.MpesaConfig;
import dev.capacytor.payments.provider.mpesa.model.SimulatePushRequestDto;
import dev.capacytor.payments.provider.mpesa.model.SimulateStkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

@RequiredArgsConstructor
public class MpesaRestClient {

    final MpesaConfig mpesaConfig;

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    ResponseEntity<SimulateStkResponseDto> simulatePush(SimulatePushRequestDto request) throws MpesaApiException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(LocalDateTime.now());

        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("BusinessShortCode", mpesaConfig.shortCode());
        requestBody.put("Password", Base64
                .getEncoder()
                .encodeToString((mpesaConfig.shortCode() + mpesaConfig.passkey() + timestamp).getBytes()));
        requestBody.put("Timestamp", timestamp);
        requestBody.put("TransactionType", "CustomerPayBillOnline");
        requestBody.put("Amount", request.amount().toString());
        requestBody.put("PartyA", request.phoneNumber());
        requestBody.put("PartyB", mpesaConfig.shortCode());
        requestBody.put("PhoneNumber", request.phoneNumber());
        requestBody.put("CallBackURL", "https://webhook.site/7a7b7b7b-7b7b-7b7b-7b7b-7b7b7b7b7b7b");
        requestBody.put("AccountReference", request.accountReference());
        requestBody.put("TransactionDesc", request.transactionDesc());

        var response = restTemplate()
                .postForEntity(mpesaConfig.url() + "/mpesa/c2b/v1/simulate", requestBody, SimulateStkResponseDto.class);

        if (response.getBody() == null) {
            throw new MpesaApiException("Push response is null", MpesaApiException.ErrorCode.NO_RESPONSE);
        } else {
            return response;
        }
    }
}
