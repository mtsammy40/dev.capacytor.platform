package dev.capacytor.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("authorization_consents")
public record AuthorizationConsent(
        @Id
        String id,
        String registeredClientId,
        String principalName,
        Set<String> authorities) {
}
