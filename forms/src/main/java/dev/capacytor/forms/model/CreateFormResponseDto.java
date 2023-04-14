package dev.capacytor.forms.model;

import dev.capacytor.forms.commons.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record CreateFormResponseDto(
        @NotBlank(message = "formId cannot be blank")
        @Max(value = Constants.MAX_TEXT_LENGTH, message = "FormId has exceeded the maximum length")
        String formId,
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
