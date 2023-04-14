package dev.capacytor.auth.config;

import dev.capacytor.auth.repository.AuthorizationConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Mongo0auth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private final AuthorizationConsentRepository authorizationConsentRepository;
    private final RegisteredClientRepository registeredClientRepository;
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        authorizationConsentRepository.save(toAuthorizationConsent(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        authorizationConsentRepository.deleteById(authorizationConsent.getRegisteredClientId());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return authorizationConsentRepository.findByRegisteredClientId(registeredClientId)
                .map(this::toOAuth2AuthorizationConsent)
                .orElseThrow(() -> new IllegalArgumentException("Authorization Consent not found"));
    }

    private OAuth2AuthorizationConsent toOAuth2AuthorizationConsent(dev.capacytor.auth.entity.AuthorizationConsent authorizationConsent) {
        return OAuth2AuthorizationConsent
                .withId(authorizationConsent.registeredClientId(), authorizationConsent.principalName())
                .authorities(grantedAuthorities -> {
                    for (String authority : authorizationConsent.authorities()) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                    }
                })
                .build();
    }

    private dev.capacytor.auth.entity.AuthorizationConsent toAuthorizationConsent(OAuth2AuthorizationConsent authorizationConsent) {
        return new dev.capacytor.auth.entity.AuthorizationConsent(
                authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName(),
                authorizationConsent.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
        );
    }
}
