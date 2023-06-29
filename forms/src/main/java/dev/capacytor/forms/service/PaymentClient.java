package dev.capacytor.forms.service;

import dev.capacytor.forms.configuration.CapacytorNetworkProperties;
import dev.capacytor.forms.model.payment.CreatePaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentClient {
    private final CapacytorNetworkProperties capacytorNetworkProperties;
    private RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri(capacytorNetworkProperties.getPayments().getBase())
                .additionalInterceptors((request, body, execution) -> {
                    log.info("=================================================================");
                    log.info("Requesting {} {} {}", request.getMethod(), request.getURI(), new String(body));
                    var response = execution.execute(request, body);
                    log.info("Response {}", response.getStatusCode());
                    log.info("=================================================================");
                    return response;
                })
                .build();
    }

    public CreatePaymentResponse createPayment(BigDecimal amount, String currency, String description, String reference, String paymentType) {
        var createPaymentRequest = Map.of(
                "reference", reference,
                "amount", amount,
                "currency", currency,
                "description", description,
                "paymentType", paymentType
        );
        var response = getRestTemplate()
                .postForEntity(capacytorNetworkProperties.getPayments().getCreatePayment(), createPaymentRequest, CreatePaymentResponse.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            if(response.getBody() != null) {
                return response.getBody();
            } else {
                throw new IllegalStateException("Failed to create payment: [Empty Body]");
            }
        } else {
            throw new IllegalStateException("Failed to create payment: [" + response.getStatusCode() + "]");
        }
    }
}
