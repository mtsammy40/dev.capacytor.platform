package dev.capacytor.payments.controller;

import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.config.NetProperties;
import dev.capacytor.payments.model.CreatePaymentRequest;
import dev.capacytor.payments.model.PaymentResponse;
import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController("apiController")
@RequestMapping(Constants.Endpoints.API_V1_PAYMENTS)
@RequiredArgsConstructor
public class ApiController {
    private final PaymentService paymentService;
    private final NetProperties networkProperties;

    @PostMapping("")
    ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        var payment = paymentService.create(request);
        var paymentLink = networkProperties.getDomain() +
                "/" +
                Constants.Endpoints.WEB_V1_PAYMENTS +
                "/" +
                payment.getId();
        return ResponseEntity
                .created(URI.create(paymentLink))
                .body(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId().toString()));
    }

    @GetMapping("/{id}")
    ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        var payment = paymentService.get(id);
        return ResponseEntity
                .ok(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId().toString()));
    }

}








