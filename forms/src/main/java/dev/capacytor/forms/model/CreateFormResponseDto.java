package dev.capacytor.forms.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record CreateFormResponseDto(
        @NotNull(message = "Responses are required")
        @NotEmpty(message = "Responses cannot be empty")
        Map<String, SectionResponse> sections,
        String paymentId
) {
    public record SectionResponse(
            @NotNull(message = "Field Responses are required")
            @NotEmpty(message = "Field Responses cannot be empty")
            Map<String, List<String>> fields) {
    }
}
