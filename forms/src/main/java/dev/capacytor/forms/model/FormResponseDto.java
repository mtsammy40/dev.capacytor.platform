package dev.capacytor.forms.model;

import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.entity.shared.PaymentData;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FormResponseDto extends RepresentationModel<FormResponseDto> {
    private String id;
    private String formId;
    private Map<String, FormResponse.SectionResponse> sections;
    private FormResponse.FormResponseStatus status;
    private FormResponse.UserData userData;
    private PaymentData paymentData;
}
