package dev.capacytor.auth.config;

import dev.capacytor.auth.entity.Client;
import dev.capacytor.auth.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Objects;

@RequiredArgsConstructor
public class MongoRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(toClient(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        var client = clientRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return toRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return null;
    }

    private RegisteredClient toRegisteredClient(Client client) {
        var registeredClientBuilder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientName(client.getClientName())
                .clientSettings(ClientSettings
                        .builder()
                        .settings(currentSettings -> {
                            if (Objects.isNull(currentSettings)) {
                                return;
                            }
                            currentSettings.putAll(client.getClientSettings());
                        })
                        .build());

        if(Objects.nonNull(client.getClientAuthenticationMethods())) {
            for (String clientAuthenticationMethod : client.getClientAuthenticationMethods()) {
                registeredClientBuilder.clientAuthenticationMethod(new ClientAuthenticationMethod(clientAuthenticationMethod));
            }
        }

        if(Objects.nonNull(client.getAuthorizationGrantType())) {
            for (String authorizationGrantType : client.getAuthorizationGrantType()) {
                registeredClientBuilder.authorizationGrantType(new AuthorizationGrantType(authorizationGrantType));
            }
        }

        if(Objects.nonNull(client.getRedirectUri())) {
            for (String uri : client.getRedirectUri()) {
                registeredClientBuilder.redirectUri(uri);
            }
        }

        if(Objects.nonNull(client.getScope())) {
            for (String scope : client.getScope()) {
                registeredClientBuilder.scope(scope);
            }
        }

        return registeredClientBuilder.build();
    }

    private Client toClient(RegisteredClient registeredClient) {
        return new Client(
                registeredClient.getId(),
                registeredClient.getClientId(),
                registeredClient.getClientSecret(),
                registeredClient.getClientName(),
                registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).toArray(String[]::new),
                registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toArray(String[]::new),
                registeredClient.getRedirectUris().toArray(String[]::new),
                registeredClient.getScopes().toArray(String[]::new),
                registeredClient.getClientSettings().getSettings()
        );
    }
}
