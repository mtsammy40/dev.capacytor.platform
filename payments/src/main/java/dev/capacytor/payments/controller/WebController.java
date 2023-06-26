package dev.capacytor.payments.controller;

import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.exception.PaymentProcessingException;
import dev.capacytor.payments.model.PayRequest;
import dev.capacytor.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
    @Controller("publicPaymentsController")
@RequestMapping(Constants.Endpoints.WEB_V1_PAYMENTS)
@RequiredArgsConstructor
public class WebController {
    private final PaymentService paymentService;

    @GetMapping("{paymentId}/checkout")
    String checkout(@PathVariable() String paymentId, Model model) {
        log.info("Checkout for paymentId: {}", paymentId);
        var checkoutData = paymentService.getCheckoutData(paymentId);
        var actionUrl = Constants.Endpoints.WEB_V1_PAYMENTS + "/pay";
        model.addAttribute("checkoutData", checkoutData);
        model.addAttribute("action", actionUrl);
        model.addAttribute("payRequest", PayRequest.builder().paymentId(paymentId).build());
        return "checkout";
    }

    @PostMapping("/pay")
    String pay(PayRequest payRequest, BindingResult result, Model model) throws PaymentProcessingException {
        log.info("Pay request: {}", payRequest);
        if (result.hasErrors()) {
            result.getAllErrors()
                    .forEach(e -> log.error(e.toString()));
            model.addAttribute("errors", "Please try again");
            return checkout(payRequest.getPaymentId(), model);
        }
        var payment = paymentService.pay(payRequest);
        var checkStatusUrl = Constants.Endpoints.WEB_V1_PAYMENTS + "/" + payment.getId() + "/status?count=0";
        model.addAttribute("payment", payment);
        model.addAttribute("action", checkStatusUrl);
        return "redirect:" + checkStatusUrl;
    }

    @GetMapping("{paymentId}/status")
    String status(@PathVariable() String paymentId, @RequestParam() Integer count, Model model) {
        log.info("Checking status for paymentId: {}", paymentId);
        var paymentStatus = paymentService.checkStatus(paymentId);
        model.addAttribute("paymentStatus", paymentStatus);
        return "payment-status";
    }

}
