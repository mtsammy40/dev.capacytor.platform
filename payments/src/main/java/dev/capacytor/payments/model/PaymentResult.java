package dev.capacytor.payments.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PaymentResult {
    private JsonNode rawData;
    private LocalDateTime received = LocalDateTime.now();

    protected PaymentResult(JsonNode rawData) {
        this.rawData = rawData;
    }
}
