package dev.capacytor.payments.service;

import dev.capacytor.payments.entity.MethodConfig;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.exception.PaymentProcessingException;
import dev.capacytor.payments.model.InitiatePaymentRequest;
import dev.capacytor.payments.provider.mpesa.model.MpesaPayData;
import dev.capacytor.payments.model.PayRequest;
import dev.capacytor.payments.model.PaymentType;
import dev.capacytor.payments.provider.mpesa.Mpesa;
import dev.capacytor.payments.provider.mpesa.PaymentMethod;
import dev.capacytor.payments.repository.MethodRepository;
import dev.capacytor.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    final PaymentRepository paymentRepository;
    final MethodRepository methodRepository;

    HashMap<PaymentType, PaymentMethod> getAvailablePaymentMethods() {
        var map = new HashMap<PaymentType, PaymentMethod>();
        var methods = methodRepository.findAllByStatus(MethodConfig.Status.ACTIVE);
        methods.forEach(method -> {
            if (method.getType().equals(PaymentType.MPESA)) {
                map.put(PaymentType.MPESA, new Mpesa(method));
            }
        });
        return map;
    }


    public Payment initiatePayment(InitiatePaymentRequest request) {
        log.info("Initiating payment for request {}", request);
        // todo: check if org is active
        // todo: check if org is within limits
        // todo: check if org is subscribed to payment method
        // todo: check if currency is allowed
        var payment = Payment
                .builder()
                .reference(request.reference())
                .description(request.description())
                .currency(request.currency())
                .amount(request.amount())
                .paymentType(request.paymentType())
                .status(Payment.PaymentStatus.PENDING)
                .originatorOrgId("1234567890")
                .build();
        paymentRepository.save(payment);
        return payment;
    }

    public Payment pay(String paymentId, PayRequest payRequest) throws PaymentProcessingException {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentProcessingException("Payment not found"));
        PaymentMethod paymentMethod = getAvailablePaymentMethods().get(payment.getPaymentType());
        if (paymentMethod == null) {
            throw new PaymentProcessingException("Payment method not available");
        }
        paymentMethod.prepare(payment, MpesaPayData.builder().phoneNumber(payRequest.phoneNumber()).build());
        paymentMethod.pay(payment);
        paymentRepository.save(payment);
        return payment;
    }

    public void processResult(String paymentId, Map<String, Object> result) {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        PaymentMethod paymentMethod = getAvailablePaymentMethods().get(payment.getPaymentType());
        if (paymentMethod == null) {
            throw new RuntimeException("Payment method not available");
        }
        paymentMethod.processResults(payment, result);
        paymentRepository.save(payment);
    }
}
