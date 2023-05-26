package dev.capacytor.payments.model;

import dev.capacytor.payments.entity.MethodConfig;

import java.util.HashMap;

public record CreateMethodConfigRequest(
        String name,
        String description,
        PaymentType type,
        HashMap<String, Object> integrationConfig,
        MethodConfig.OperationConfig operationConfig
) {
}
