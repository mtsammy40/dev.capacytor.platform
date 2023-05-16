package dev.capacytor.forms.commons.form.stage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class StageConfiguration {
    @JsonIgnore
    public Boolean isActive() {
        return false;
    }
}
