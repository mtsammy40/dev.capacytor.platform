package dev.capacytor.auth.repository;

import dev.capacytor.auth.entity.AuthorizationConsent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthorizationConsentRepository extends MongoRepository<AuthorizationConsent, String> {
    Optional<AuthorizationConsent> findByRegisteredClientId(String registeredClientId);
}
