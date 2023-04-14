package dev.capacytor.auth.config;

import dev.capacytor.auth.entity.AuthorizationToken;
import dev.capacytor.auth.repository.AuthorizationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
public class MongoOauth2AuthorizationService implements OAuth2AuthorizationService {
    private final AuthorizationTokenRepository authorizationTokenRepository;
    private final RegisteredClientRepository clientRepository;
    @Override
    public void save(OAuth2Authorization authorization) {
        authorizationTokenRepository.save(toAuthorizationToken(authorization));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        authorizationTokenRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        var authorizationToken = authorizationTokenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Authorization not found"));
        return toOAuth2Authorization(authorizationToken);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        var authorizationToken = authorizationTokenRepository.findByTokens(token)
                .orElseThrow(() -> new IllegalArgumentException("Authorization Token not found"));
        return toOAuth2Authorization(authorizationToken);
    }

    private AuthorizationToken toAuthorizationToken(OAuth2Authorization authorization) {
        var tokens = new HashMap<String, AuthorizationToken.Token>();
        var accessToken = authorization.getAccessToken();
        if(Objects.nonNull(accessToken)) {
            tokens.put(AuthorizationToken.Token.ACCESS_TOKEN, toToken(accessToken, OAuth2AccessToken.class));
        }
        var refreshToken = authorization.getRefreshToken();
        if(Objects.nonNull(refreshToken)) {
            tokens.put(AuthorizationToken.Token.REFRESH_TOKEN, toToken(refreshToken, OAuth2RefreshToken.class));
        }
        return new AuthorizationToken(
                authorization.getId(),
                authorization.getRegisteredClientId(),
                authorization.getPrincipalName(),
                authorization.getAuthorizationGrantType(),
                authorization.getAuthorizedScopes(),
                tokens,
                authorization.getAttributes()
        );
    }

    private <T extends OAuth2Token> AuthorizationToken.Token toToken(OAuth2Authorization.Token<T> abstractOAuth2Token, Class<T> tokenClass) {
        if(tokenClass.equals(OAuth2RefreshToken.class) && abstractOAuth2Token.getToken() instanceof OAuth2RefreshToken refreshToken) {
            return new AuthorizationToken.Token(
                    OAuth2AccessToken.TokenType.BEARER.getValue(),
                    refreshToken.getTokenValue(),
                    AuthorizationToken.Token.REFRESH_TOKEN,
                    Objects.requireNonNull(refreshToken.getIssuedAt()).toEpochMilli(),
                    Objects.requireNonNull(refreshToken.getExpiresAt()).toEpochMilli()
            );
        } else if(tokenClass.equals(OAuth2RefreshToken.class) && abstractOAuth2Token.getToken() instanceof OAuth2AccessToken accessToken) {
            return new AuthorizationToken.Token(
                    accessToken.getTokenType().getValue(),
                    accessToken.getTokenValue(),
                    AuthorizationToken.Token.ACCESS_TOKEN,
                    Objects.requireNonNull(accessToken.getIssuedAt()).toEpochMilli(),
                    Objects.requireNonNull(accessToken.getExpiresAt()).toEpochMilli()
            );
        } else {
            throw new IllegalArgumentException("Unknown token type");
        }
    }

    private OAuth2Authorization toOAuth2Authorization(AuthorizationToken authorizationToken) {
        var builder = OAuth2Authorization.withRegisteredClient(
                Objects.requireNonNull(clientRepository.findById(authorizationToken.id()))
        )
                .principalName(authorizationToken.principalName())
                .authorizationGrantType(authorizationToken.authorizationGrantType())
                .authorizedScopes(authorizationToken.authorizedScopes())
                .attributes(attribute -> attribute.putAll(authorizationToken.attributes()));
        var tokens = authorizationToken.tokens();
        if(tokens.containsKey(AuthorizationToken.Token.ACCESS_TOKEN)) {
            builder.accessToken(toOAuth2AccessToken(tokens.get(AuthorizationToken.Token.ACCESS_TOKEN)));
        }
        if(tokens.containsKey(AuthorizationToken.Token.REFRESH_TOKEN)) {
            builder.refreshToken(toOAuth2RefreshToken(tokens.get(AuthorizationToken.Token.REFRESH_TOKEN)));
        }
        return builder.build();
    }

    private OAuth2AccessToken toOAuth2AccessToken(AuthorizationToken.Token token) {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                token.tokenValue(),
                Instant.ofEpochMilli(token.tokenIssuedAt()),
                Instant.ofEpochMilli(token.tokenExpiresAt())
        );
    }
    private OAuth2RefreshToken toOAuth2RefreshToken(AuthorizationToken.Token token) {
        return new OAuth2RefreshToken(
                token.tokenValue(),
                Instant.ofEpochMilli(token.tokenIssuedAt()),
                Instant.ofEpochMilli(token.tokenExpiresAt())
        );
    }
}

