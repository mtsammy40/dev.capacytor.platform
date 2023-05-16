package dev.capacytor.payments.controller;

import dev.capacytor.payments.model.InitiatePaymentRequest;
import dev.capacytor.payments.model.PaymentResponse;
import dev.capacytor.payments.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/api")
public class PublicApiController {
    PaymentService paymentService;

    @PostMapping("/payment")
    ResponseEntity<PaymentResponse> initiatePayment(InitiatePaymentRequest request) {
        var payment = paymentService.initiatePayment(request);
        return ResponseEntity
                .ok(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId()));
    }

}








