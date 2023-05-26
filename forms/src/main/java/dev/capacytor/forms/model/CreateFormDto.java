package dev.capacytor.forms.model;

import dev.capacytor.forms.commons.Constants;
import dev.capacytor.forms.commons.form.stage.FillingStage;
import dev.capacytor.forms.commons.form.stage.PaymentStage;
import dev.capacytor.forms.commons.form.stage.VerificationStage;
import dev.capacytor.forms.entity.Form;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
public record CreateFormDto(
        @NotBlank(message = "Name is required")
        @Max(value = Constants.MAX_TEXT_LENGTH, message = "Name exceeded max allowed characters")
        String name,
        @Max(value = Constants.MAX_TEXT_LENGTH, message = "Description exceeded max allowed characters")
        String description,
        @NotEmpty(message = "Sections are required")
        @NotNull(message = "Sections are required")
        @Max(value = Constants.MAX_SECTIONS_ALLOWED, message = "Max sections allowed exceeded")
        List<@Valid Section> sections,
        Configuration configuration
) {
        public record Section(
                @NotBlank(message = "Name is required")
                @Max(value = Constants.MAX_TEXT_LENGTH, message = "Name exceeded max allowed characters")
                String name,
                @Max(value = Constants.MAX_TEXT_LENGTH, message = "Description exceeded max allowed characters")
                String description,
                @NotEmpty(message = "Fields are required")
                @NotNull(message = "Fields are required")
                @Max(value = Constants.MAX_FIELDS_ALLOWED, message = "Max fields allowed exceeded")
                List<@Valid Field> fields
        ) {
        }

        public record Field(
                @NotBlank(message = "Name is required")
                @Max(value = Constants.MAX_TEXT_LENGTH, message = "Name exceeded max allowed characters")
                String name,
                @NotBlank(message = "Type is required")
                @Max(value = Constants.MAX_TEXT_LENGTH, message = "Type exceeded max allowed characters")
                Form.Field.FieldType type,
                @NotEmpty(message = "Options are required")
                @NotNull(message = "Options are required")
                @Max(value = Constants.MAX_OPTIONS_ALLOWED, message = "Max options allowed exceeded")
                List<Options> options
        ) {
                public record Options(
                        @NotEmpty(message = "Option value are required")
                        @NotNull(message = "Options are required")
                        @Max(value = Constants.MAX_TEXT_LENGTH, message = "Max options allowed exceeded")
                        String value
                ) {
                }
        }

        public record Configuration(
                @Valid
                StageDto stageConfiguration
        ) {
        }

        @Data
        public static class StageDto {
                @Valid
                FillingStage.FillingStageConfiguration fillingStageConfiguration;
                @Valid
                PaymentStage.PaymentStageConfiguration paymentStageConfiguration;
                @Valid
                VerificationStage.VerificationStageConfiguration verificationStageConfiguration;
        }

}
