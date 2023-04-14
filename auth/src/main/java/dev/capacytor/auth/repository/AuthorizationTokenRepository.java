package dev.capacytor.auth.repository;

import dev.capacytor.auth.entity.AuthorizationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface AuthorizationTokenRepository extends MongoRepository<AuthorizationToken, String> {
    @Query("{ 'tokens.tokenValue': ?0 }")
    Optional<AuthorizationToken> findByTokens(String token);
}
