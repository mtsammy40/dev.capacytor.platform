package dev.capacytor.forms.commons.form.stage;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.entity.shared.Amount;
import dev.capacytor.forms.entity.shared.PaymentData;
import dev.capacytor.forms.model.FormResponseSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentStage extends Stage {
    public static final String NAME = "payment_stage";
    private PaymentStageConfiguration configuration;

    @Override
    public Stage getNext(Form form, FormResponse response) {
        return getPossibleNextStage(form, CompletedStage.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(FormResponseSession formResponseSession) {
        log.info("Form response {} is in payment stage", formResponseSession.getResponse().getId());
        var paymentStageResult = formResponseSession
                .getForm()
                .getConfiguration()
                .getStages()
                .stream()
                .filter(stage -> stage.getName().equalsIgnoreCase(NAME))
                .findFirst();
        paymentStageResult.ifPresent(paymentStage -> {
            var paymentStageConfiguration = ((PaymentStage) paymentStage).getConfiguration();
            var paymentDetails = formResponseSession.getPaymentClient().createPayment(
                    paymentStageConfiguration.getAmount(),
                    paymentStageConfiguration.getCurrency(),
                    paymentStageConfiguration.getDescription(),
                    formResponseSession.getResponse().getId(),
                    "MPESA"
            );
            formResponseSession.getResponse().setPaymentData(PaymentData.builder()
                    .paymentId(paymentDetails.getPaymentId())
                    .paymentAmount(Amount.from(String.valueOf(paymentStageConfiguration.getAmount()), paymentStageConfiguration.getCurrency()))
                    .paymentStatus(PaymentData.PaymentStatus.PENDING)
                    .paymentMethod(PaymentData.PaymentMethod.MOBILE_MONEY)
                    .paymentUrl(paymentDetails.getPaymentUrl())
                    .build());
        });
    }

    @Override
    public boolean requiresExternalAction() {
        return true;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentStageConfiguration extends StageConfiguration {
        private BigDecimal amount;
        private String currency;
        private String description;
        @Builder.Default
        private Boolean stageIsEnabled = true;
        private BigDecimal minimumAmountAllowed;

        @Override
        public Boolean isActive() {
            return stageIsEnabled;
        }
    }
}
