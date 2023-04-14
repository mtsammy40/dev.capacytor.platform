package dev.capacytor.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("client_auth_config")
public class Client {
    private String id;
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String[] clientAuthenticationMethods;
    private String[] authorizationGrantType;
    private String[] redirectUri;
    private String[] scope;
    private Map<String, Object> clientSettings;
}