package dev.capacytor.payments.provider.mpesa;

import com.fasterxml.jackson.databind.JsonNode;
import dev.capacytor.payments.entity.Payment;
import dev.capacytor.payments.model.PayData;
import dev.capacytor.payments.provider.mpesa.model.MpesaConfig;
import dev.capacytor.payments.provider.mpesa.model.MpesaPayData;
import dev.capacytor.payments.provider.mpesa.model.MpesaPaymentResult;
import dev.capacytor.payments.provider.mpesa.model.SimulatePushRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class Mpesa implements PaymentMethod {

    private final MpesaConfig mpesaConfig;

    private MpesaRestClient mpesaRestClient() {
        return new MpesaRestClient(mpesaConfig);
    }

    @Override
    public <P extends PayData> void prepare(Payment payment, P payData) {
        if (payData instanceof MpesaPayData mpesaPayData) {
            var mpesaInfo = payment.getInfo().getMpesaInfo() == null ? new Payment.MpesaInfo() : payment.getInfo().getMpesaInfo();
            mpesaInfo.setPhoneNumber(mpesaPayData.getPhoneNumber());
            payment.getInfo().setMpesaInfo(mpesaInfo);
        } else {
            throw new IllegalArgumentException("Invalid pay data type");
        }
    }

    @Override
    public void pay(Payment payment) {
        var mpesaInfo = payment.getInfo().getMpesaInfo() == null ? new Payment.MpesaInfo() : payment.getInfo().getMpesaInfo();
        try {
            payment.setStatus(Payment.PaymentStatus.PROCESSING);
            var checkoutRequestId = initiatePush(payment);
            mpesaInfo.setCheckoutRequestID(checkoutRequestId);
            payment.setProviderReference(checkoutRequestId);
            payment.getInfo().setMpesaInfo(mpesaInfo);
        } catch (MpesaApiException e) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            mpesaInfo.setErrorMessage(e.getMessage());
            payment.getInfo().setMpesaInfo(mpesaInfo);
            throw new IllegalStateException(e.getErrorCode().getCode());
        }
    }

    @Override
    public void processResults(Payment payment, JsonNode paymentResult) {
        var mpesaPaymentResult = new MpesaPaymentResult(paymentResult);
        if (mpesaPaymentResult.isSuccess()) {
            payment.setStatus(Payment.PaymentStatus.PAID);
            payment.getInfo().getMpesaInfo().setReceipt(mpesaPaymentResult.getTrxReceipt());
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.getInfo().getMpesaInfo().setErrorMessage(mpesaPaymentResult.getResultDesc());
        }
        payment.setCompletedAt(LocalDateTime.now());
    }

    public static String extractProviderReference(JsonNode paymentResult) {
        return new MpesaPaymentResult(paymentResult).getProviderReference();
    }

    String initiatePush(Payment payment) throws MpesaApiException {
        var pushResponse = mpesaRestClient()
                .authenticate()
                .simulatePush(SimulatePushRequestDto
                        .builder()
                        .amount(payment.getAmount())
                        .accountReference(payment.getId().toString())
                        .transactionDesc(payment.getDescription())
                        .phoneNumber(payment.getInfo().getMpesaInfo().getPhoneNumber())
                        .build());
        if (pushResponse.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(pushResponse.getBody()).checkoutRequestID();
        } else {
            throw new MpesaApiException(Objects.requireNonNull(pushResponse.getBody()).customerMessage(), MpesaApiException.ErrorCode.PUSH_FAILED);
        }
    }
}
