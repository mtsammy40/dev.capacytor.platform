package dev.capacytor.payments.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.capacytor.payments.entity.Client;
import dev.capacytor.payments.entity.MethodConfig;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.exception.PaymentProcessingException;
import dev.capacytor.payments.model.CreatePaymentRequest;
import dev.capacytor.payments.model.Currency;
import dev.capacytor.payments.model.PayRequest;
import dev.capacytor.payments.model.PaymentType;
import dev.capacytor.payments.provider.cash.Cash;
import dev.capacytor.payments.provider.mpesa.Mpesa;
import dev.capacytor.payments.provider.PaymentMethod;
import dev.capacytor.payments.provider.mpesa.model.MpesaConfig;
import dev.capacytor.payments.provider.mpesa.model.MpesaPayData;
import dev.capacytor.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    final PaymentRepository paymentRepository;
    final MethodService methodService;
    final ClientService clientService;

    EnumMap<PaymentType, PaymentMethod> getAvailablePaymentMethods() {
        var map = new EnumMap<PaymentType, PaymentMethod>(PaymentType.class);
        var methods = methodService.findByStatus(MethodConfig.Status.ACTIVE);
        methods.forEach(method -> {
            if (method.getType().equals(PaymentType.MPESA)) {
                var mpesaConfig = new ObjectMapper()
                        .convertValue(method.getIntegrationConfig(), MpesaConfig.class);
                map.put(PaymentType.MPESA, new Mpesa(mpesaConfig));
            } else if (method.getType().equals(PaymentType.CASH)) {
                map.put(PaymentType.CASH, new Cash());
            }
        });
        return map;
    }


    public Payment create(CreatePaymentRequest request) {
        log.info("Initiating payment for request {}", request);
        var clientId = "testClient";
        var client = clientService.getClient(clientId);
        if (!client.getStatus().equals(Client.Status.ACTIVE)) {
            throw new IllegalArgumentException("Client is not active");
        }
        var clientIsSubscribedToPaymentMethod = client
                .getPaymentConfiguration()
                .getAllowedMethods()
                .stream()
                .anyMatch(method -> method.getPaymentType().equals(request.paymentType()));

        if(!clientIsSubscribedToPaymentMethod) {
            throw new IllegalArgumentException("Client is not subscribed to payment method");
        }

        if(!client.getPaymentConfiguration().getAllowedCurrencies().contains(Currency.valueOf(request.currency()))) {
            throw new IllegalArgumentException("Client is not subscribed to payment currency");
        }

        // todo: check if client is within limits
        var payment = Payment
                .builder()
                .reference(request.reference())
                .description(request.description())
                .currency(request.currency())
                .amount(request.amount())
                .paymentType(request.paymentType())
                .status(Payment.PaymentStatus.PENDING)
                .originatorOrgId(clientId)
                .build();
        paymentRepository.save(payment);
        return payment;
    }

    public Payment get(@NonNull String id) {
        return paymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));
    }

    public Payment pay(String paymentId, PayRequest payRequest) throws PaymentProcessingException {
        var payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new PaymentProcessingException("Payment not found"));
        PaymentMethod paymentMethod = getAvailablePaymentMethods().get(payment.getPaymentType());
        if (paymentMethod == null) {
            throw new PaymentProcessingException("Payment method not available");
        }
        paymentMethod
                .prepare(payment, MpesaPayData.builder().phoneNumber(payRequest.phoneNumber()).build())
                .initiate();
        return paymentRepository.save(payment);
    }

    public void processResult(String paymentId, JsonNode result) {
        var payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        processResult(payment, result);
    }

    public void processMpesaResult(JsonNode result) {
        var checkoutRequestId = Mpesa.extractProviderReference(result);
        var payment = paymentRepository.findPaymentByProviderReference(checkoutRequestId)
                .orElseThrow(() -> new RuntimeException("Payment not found :: "+ checkoutRequestId));
        processResult(payment, result);
    }

    private void processResult(Payment payment, JsonNode result) {
        PaymentMethod paymentMethod = getAvailablePaymentMethods().get(payment.getPaymentType());
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method not available");
        }
        paymentMethod.processResults(payment, result);
        paymentRepository.save(payment);
    }
}
