package dev.capacytor.forms.entity;

import dev.capacytor.forms.commons.form.stage.Stage;
import dev.capacytor.forms.entity.shared.PaymentData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "form-responses")
@Slf4j
public class FormResponse {
    @Transient
    private boolean isValid = false;

    private String id;
    private String formId;
    private Map<String, SectionResponse> sections;
    private FormResponseStatus status;
    private UserData userData;
    private PaymentData paymentData;

    @Data
    @Builder
    @AllArgsConstructor
    public static class SectionResponse {
        Map<String, List<String>> fields;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserData {
        private String userId;
        private String email;
        private String ipAddress;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class FormResponseStatus {
        private boolean isComplete;
        private Stage currentStage;
        private Stage[] pendingStages;
        private Stage[] completedStages;
    }

    public void validate(Form form) {
        log.info("Validating Form Response for formResponseId : {}", id);
        List<String> errors = new ArrayList<>();
        for (Map.Entry<String, SectionResponse> entry : sections.entrySet()) {
            log.debug("validating section : {}", entry.getKey());
            var formSection = form.getSections()
                    .stream()
                    .filter(section -> section.getId().equals(entry.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Section not found"));

            for (String fieldId : entry.getValue().getFields().keySet()) {
                var formField = formSection.getFields()
                        .stream()
                        .filter(field -> field.getId().equals(fieldId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Field not found"));
                var fieldResponse = entry.getValue().getFields().get(fieldId);
                log.debug("validating field : id {}, type: {}, values: {}", fieldId, formField.getType(), fieldResponse);
                validateField(formField, fieldResponse, errors);
            }
        }

        if (!errors.isEmpty()) {
            isValid = false;
            throw new IllegalArgumentException("Form errors :".concat(String.join(", ", errors)));
        } else {
            isValid = true;
            log.info("Form Response is valid");
        }
    }

    void validateField(Form.Field formField, List<String> fieldResponse, List<String> errors) {
        if (formField.getIsRequired() && (fieldResponse.isEmpty() || fieldResponse.get(0).isBlank())) {
            errors.add("Field " + formField.getName() + " is required");
        }

        switch (formField.getType()) {
            case CHECKBOX -> {
                canHaveOneValueMax(formField, fieldResponse, errors);
                if (!fieldResponse.isEmpty() && !fieldResponse.contains("checked")) {
                    errors.add("Field " + formField.getName() + " can only have value 'checked'");
                }
            }
            case TEXT, NUMBER, TEXTAREA, TIME, DATE -> canHaveOneValueMax(formField, fieldResponse, errors);
            case RADIO, SELECT -> {
                canHaveOneValueMax(formField, fieldResponse, errors);
                canHaveOnlyValuesFromOptions(formField, fieldResponse, errors);
            }
            default -> throw new IllegalStateException("Unexpected value: " + formField.getType());
        }
    }
    
    void canHaveOneValueMax(Form.Field formField, List<String> fieldResponse, List<String> errors) {
        if (fieldResponse.size() > 1) {
            errors.add("Field " + formField.getName() + " can only have one value");
        }   
    }

    void canHaveOnlyValuesFromOptions(Form.Field formField, List<String> fieldResponse, List<String> errors) {
        if (!new HashSet<>(formField.getOptions()).containsAll(fieldResponse)) {
            errors.add("Field " + formField.getName() + " can only have values from options");
        }
    }
}
