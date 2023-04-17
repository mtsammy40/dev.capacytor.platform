package dev.capacytor.forms.commons.form.stage;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.model.FormResponseSession;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class VerificationStage extends Stage {
    public static final String NAME = "verification_stage";
    private final VerificationStageConfiguration configuration;

    @Override
    public Stage getNext(Form form, FormResponse response) {
       return getPossibleNextStage(form, PaymentStage.class, CompletedStage.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(FormResponseSession formResponseSession) {
        throw new UnsupportedOperationException("Verification stage is not implemented yet");
    }

    @Override
    public boolean requiresExternalAction() {
        return true;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationStageConfiguration extends StageConfiguration {
        private Boolean stageIsEnabled = true;

        @Override
        public Boolean isActive() {
            return stageIsEnabled;
        }
    }
}
