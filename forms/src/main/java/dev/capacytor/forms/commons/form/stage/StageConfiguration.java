package dev.capacytor.forms.commons.form.stage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class StageConfiguration {
    public Boolean isActive() {
        return false;
    }
}
