package dev.capacytor.payments.controller.platform;

import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.exception.PaymentProcessingException;
import dev.capacytor.payments.model.PayRequest;
import dev.capacytor.payments.model.PaymentResponse;
import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(Constants.Endpoints.PLATFORM_V1_PAYMENTS)
@RequiredArgsConstructor
public class Payments {

    private final PaymentService paymentService;

    @PostMapping("/{id}/pay")
    ResponseEntity<PaymentResponse> pay(@PathVariable String id, @RequestBody() PayRequest payRequest) throws PaymentProcessingException {
        var payment = paymentService.pay(id, payRequest);
        return ResponseEntity
                .ok(new PaymentResponse(payment.getReference(), payment.getStatus(), payment.getId().toString()));
    }

}
