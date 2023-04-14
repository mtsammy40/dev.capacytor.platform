package dev.capacytor.forms.commons;

import org.springframework.lang.NonNull;

public class IdGenerator {
    public static String generate(@NonNull String prefix) {
        return prefix.concat(java.util.UUID.randomUUID().toString());
    }
}
