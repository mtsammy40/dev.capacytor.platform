package dev.capacytor.forms.commons.form.stage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.capacytor.forms.commons.Constants;
import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.model.FormResponseSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.springframework.data.annotation.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class FillingStage extends Stage {
    public static final String NAME = "filling_stage";

    @Valid
    private final FillingStageConfiguration configuration;

    @Override
    public Stage getNext(Form form, FormResponse response) {
        return getPossibleNextStage(form, VerificationStage.class, PaymentStage.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(FormResponseSession formResponseSession) {
        formResponseSession.getResponse().validate(formResponseSession.getForm());
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FillingStageConfiguration extends StageConfiguration {
        @Max(value = Constants.MAX_TEXT_LENGTH, message = "Header image size exceeds maximum allowed size")
        private String headerImageUrl;

        @Transient
        @Builder.Default
        @JsonIgnore
        private Boolean stageIsEnabled = true;

        @Override
        public Boolean isActive() {
            return stageIsEnabled;
        }

        public void setStageIsEnabled(Boolean stageIsEnabled) {
            throw new UnsupportedOperationException("FillingStage is always active");
        }
    }
}
