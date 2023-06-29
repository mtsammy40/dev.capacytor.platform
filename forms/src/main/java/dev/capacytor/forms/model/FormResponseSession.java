package dev.capacytor.forms.model;

import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.service.FormResponseService;
import dev.capacytor.forms.service.FormService;
import dev.capacytor.forms.service.PaymentClient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormResponseSession {
    Form form;
    FormResponse response;
    FormService formService;
    FormResponseService responseService;
    PaymentClient paymentClient;
}
