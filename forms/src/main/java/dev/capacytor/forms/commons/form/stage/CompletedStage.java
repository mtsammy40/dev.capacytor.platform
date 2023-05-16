package dev.capacytor.forms.commons.form.stage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.model.FormResponseSession;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class CompletedStage extends Stage{
    public static final String NAME = "completed_stage";

    @Override
    public Stage getNext(Form form, FormResponse response) {
        throw new IllegalCallerException("Completed stage has no next stage");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(FormResponseSession formResponseSession) {
        log.info("Form response {} is completed", formResponseSession.getResponse().getId());
        formResponseSession.getResponse().getStatus().setComplete(true);
    }

    @Override
    @JsonIgnore
    public boolean isComplete() {
        return true;
    }
}
