package dev.capacytor.forms.commons.form.stage;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import lombok.Getter;

import java.util.Arrays;

@Getter
public abstract class Stage {

    public abstract Stage getNext(Form form, FormResponse response);

    @SafeVarargs
    protected final Stage getPossibleNextStage(Form form, Class<? extends Stage>... allowedNextStages) {
        return form.getConfiguration().getStages()
                .stream()
                .filter(stage -> Arrays.asList(allowedNextStages).contains(stage.getClass()))
                .findFirst().orElseThrow(() -> new RuntimeException("No next stage found"));
    }

    public boolean isComplete() {
        return false;
    }

    public abstract String getName();

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj instanceof Stage otherStage) {
            return otherStage.getName().equals(getName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
