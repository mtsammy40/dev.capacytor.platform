package dev.capacytor.payments.controller.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Constants.Endpoints.PLATFORM_V1_WEBHOOKS)
@RequiredArgsConstructor
public class WebhookController {

    final PaymentService paymentService;

    @PostMapping("{paymentId}")
    ResponseEntity<?> webhooks(@PathVariable String paymentId, @RequestBody() String body) throws JsonProcessingException {
        log.info("Webhook incoming: {}", body);
        paymentService.processResult(paymentId, new ObjectMapper().readTree(body));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mpesa")
    ResponseEntity<?> mpesaWebhooks(@RequestBody() String body) throws JsonProcessingException {
        log.info("Mpesa Webhook incoming: {}", body);
        paymentService.processMpesaResult(new ObjectMapper().readTree(body));
        return ResponseEntity.ok().build();
    }
}
