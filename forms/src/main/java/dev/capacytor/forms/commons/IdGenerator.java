package dev.capacytor.forms.commons;

import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class IdGenerator {
    public static String generate(@NonNull String prefix) {
        return prefix.concat(java.util.UUID.randomUUID().toString());
    }
}
