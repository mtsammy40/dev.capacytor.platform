package dev.capacytor.forms.service;

import dev.capacytor.forms.commons.form.stage.Stage;
import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.entity.shared.PaymentData;
import dev.capacytor.forms.model.CreateFormResponseDto;
import dev.capacytor.forms.model.FormResponseSession;
import dev.capacytor.forms.repository.FormResponseRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FormResponseService {
    private final FormResponseRepository formResponseRepository;
    private final FormService formService;
    private final PaymentClient paymentClient;

    @Autowired
    public FormResponseService(FormResponseRepository formResponseRepository, FormService formService, PaymentClient paymentClient) {
        this.formResponseRepository = formResponseRepository;
        this.formService = formService;
        this.paymentClient = paymentClient;
    }

    public FormResponse createFormResponse(String formId, @Valid CreateFormResponseDto createFormResponseDto) {
        log.info("Creating form response for form : {}", createFormResponseDto);
        var form = formService.getForm(formId);
        var userData = getUserData();
        var formStatus = computeInitialFormReturnStatus(form);
        var paymentData = getPaymentData(createFormResponseDto.paymentId());
        var formResponse = FormResponse.builder()
                .id(generateFormResponseId())
                .formId(form.getId())
                .sections(generateSections(createFormResponseDto))
                .paymentData(paymentData)
                .userData(userData)
                .status(formStatus)
                .build();

        var responseSession = FormResponseSession.builder()
                .form(form)
                .response(formResponse)
                .formService(formService)
                .responseService(this)
                .paymentClient(paymentClient)
                .build();

        while (
                !formResponse.getStatus().isComplete() && formResponse.getStatus().getCurrentStage() != null
        ) {
            log.debug("Executing stage : {}", formResponse.getStatus().getCurrentStage().getClass().getSimpleName());
            responseSession.getResponse().getStatus().getCurrentStage().execute(responseSession);
            if (responseSession.getResponse().getStatus().getCurrentStage().requiresExternalAction()) {
                break;
            }
            responseSession.getResponse().getStatus().moveToNextStage();
        }

        return formResponseRepository.save(responseSession.getResponse());
    }

    private FormResponse.FormResponseStatus computeInitialFormReturnStatus(Form form) {
        var currentStage = form.getConfiguration().getStages().get(0);
        return FormResponse.FormResponseStatus.builder()
                .isComplete(false)
                .currentStage(currentStage)
                .pendingStages(form.getConfiguration().getStages()
                        .stream()
                        .filter(stage -> !stage.equals(currentStage))
                        .toArray(Stage[]::new))
                .completedStages(new Stage[0])
                .build();
    }

    private FormResponse.UserData getUserData() {
        // TODO: get user data from user service
        return FormResponse.UserData.builder().build();
    }

    private PaymentData getPaymentData(String paymentId) {
        // TODO: get payment data from payment service
        return PaymentData.builder()
                .paymentId(paymentId)
                .build();
    }

    public FormResponse getFormResponseById(String id) {
        return formResponseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Form Response not found"));
    }

    public List<FormResponse> getFormResponsesByFormId(String formId) {
        return formResponseRepository
                .findAllByFormId(formId);
    }

    private Map<String, FormResponse.SectionResponse> generateSections(CreateFormResponseDto createFormResponseDto) {
        return createFormResponseDto.sections().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> FormResponse.SectionResponse.builder()
                        .fields(entry.getValue().fields())
                        .build()));
    }

    private String generateFormResponseId() {
        return UUID.randomUUID().toString();
    }
}
