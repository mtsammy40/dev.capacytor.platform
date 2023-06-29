package dev.capacytor.forms.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "dev.capacytor.network")
@Configuration
@Data
public class CapacytorNetworkProperties {
    private String base;
    private Payments payments;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Payments {
        private String base;
        private String createPayment;
        private String checkout;
    }
}
