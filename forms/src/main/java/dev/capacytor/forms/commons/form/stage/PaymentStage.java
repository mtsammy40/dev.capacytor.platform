package dev.capacytor.forms.commons.form.stage;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class PaymentStage extends Stage {
    public static final String NAME = "payment_stage";
    private final PaymentStageConfiguration configuration;
    @Override
    public Stage getNext(Form form, FormResponse response) {
        return getPossibleNextStage(form, CompletedStage.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentStageConfiguration extends StageConfiguration {
        private List<String> allowedPaymentMethods;
        @Builder.Default
        private Boolean stageIsEnabled = true;

        @Override
        public Boolean isActive() {
            return stageIsEnabled;
        }
    }
}
