package dev.capacytor.forms.commons.form.stage;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
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
    public boolean isComplete() {
        return true;
    }
}
