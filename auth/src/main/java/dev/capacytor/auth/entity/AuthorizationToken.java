package dev.capacytor.auth.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Document("authorization_tokens")
public record AuthorizationToken(
        String id,
        String registeredClientId,
        String principalName,
        AuthorizationGrantType authorizationGrantType,
        Set<String> authorizedScopes,
        Map<String, Token> tokens,
        Map<String, Object> attributes) {
    public record Token(String tokenType, String tokenValue, String tokenUse, long tokenIssuedAt, long tokenExpiresAt) {
        public final static String ACCESS_TOKEN = "ACCESS_TOKEN";
        public final static String REFRESH_TOKEN = "REFRESH_TOKEN";

        public Token(String tokenType, String tokenValue, String tokenUse, long tokenIssuedAt, long tokenExpiresAt) {
            this.tokenValue = tokenValue;
            this.tokenType = tokenType;
            this.tokenIssuedAt = tokenIssuedAt;
            this.tokenExpiresAt = tokenExpiresAt;
            var tokenUseValues = List.of(ACCESS_TOKEN, REFRESH_TOKEN);
            if(!tokenUseValues.contains(tokenUse)) {
                throw new IllegalArgumentException("tokenUse must be one of " + tokenUseValues);
            }
            this.tokenUse = tokenUse;
        }
    }
}
