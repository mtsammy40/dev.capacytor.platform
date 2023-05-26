package dev.capacytor.payments.controller;

import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.model.CreatePaymentRequest;
import dev.capacytor.payments.model.PaymentResponse;
import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("paymentsController")
@RequestMapping(Constants.Endpoints.V1_PAYMENTS)
@RequiredArgsConstructor
public class Payments {
    private final PaymentService paymentService;

    @PostMapping("")
    ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        var payment = paymentService.create(request);
        return ResponseEntity
                .ok(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId().toString()));
    }

    @GetMapping("/{id}")
    ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        var payment = paymentService.get(id);
        return ResponseEntity
                .ok(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId().toString()));
    }

}








