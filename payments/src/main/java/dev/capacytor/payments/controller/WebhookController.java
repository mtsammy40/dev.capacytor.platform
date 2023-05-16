package dev.capacytor.payments.controller;

import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    final PaymentService paymentService;

    @PostMapping("{paymentId}")
    ResponseEntity<?> webhooks(@PathVariable String paymentId, @RequestBody() HashMap<String, Object> body) {
        log.info("Webhook incoming: {}", body);
        paymentService.processResult(paymentId, body);
        return ResponseEntity.ok().build();
    }
}
