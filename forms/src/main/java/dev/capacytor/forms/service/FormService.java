package dev.capacytor.forms.service;

import dev.capacytor.forms.commons.IdGenerator;
import dev.capacytor.forms.commons.form.stage.CompletedStage;
import dev.capacytor.forms.commons.form.stage.FillingStage;
import dev.capacytor.forms.commons.form.stage.PaymentStage;
import dev.capacytor.forms.commons.form.stage.VerificationStage;
import dev.capacytor.forms.entity.Form;
import dev.capacytor.forms.model.CreateFormDto;
import dev.capacytor.forms.repository.FormRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FormService {
    private final FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public Form createForm(@Valid CreateFormDto createFormDto) {
        log.info("Creating form : {}", createFormDto);
        var formBuilder = Form.builder()
                .name(createFormDto.name())
                .description(createFormDto.description())
                .sections(getSections(createFormDto))
                .configuration(getFormConfiguration(createFormDto));
        var createdForm = formRepository.save(formBuilder.build());
        log.info("Form created : {}", createdForm.getId());
        return createdForm;
    }

    public Collection<Form> getForms() {
        return formRepository.findAll();
    }

    List<Form.Section> getSections(CreateFormDto createFormDto) {
        return createFormDto.sections()
                .stream()
                .map(section -> Form.Section.builder()
                        .id(IdGenerator.generate("sc_"))
                        .name(section.name())
                        .description(section.description())
                        .fields(section.fields().stream().map(field -> Form.Field.builder()
                                .id(IdGenerator.generate("fl_"))
                                .name(field.name())
                                .type(field.type())
                                .options(field.options().stream()
                                        .map(option -> Form.Field.Option.builder()
                                                .value(option.value())
                                                .build()).toList())
                                .build()).toList())
                        .build())
                .toList();
    }

    Form.Configuration getFormConfiguration(CreateFormDto createFormDto) {
        var formConfiguration = Form.Configuration.builder().build();
        formConfiguration.getStages()
                .add(new FillingStage(FillingStage
                        .FillingStageConfiguration.builder()
                        .build()));
        if (createFormDto.configuration() != null && createFormDto.configuration().stageConfiguration() != null) {
            var stageConfig = createFormDto.configuration().stageConfiguration();
            if (stageConfig.getFillingStageConfiguration() != null) {
                if (formConfiguration.getStages().get(0) instanceof FillingStage fillingStage) {
                    if (StringUtils.hasText(stageConfig.getFillingStageConfiguration().getHeaderImageUrl())) {
                        fillingStage
                                .getConfiguration()
                                .setHeaderImageUrl(stageConfig.getFillingStageConfiguration().getHeaderImageUrl());
                    }
                }
            }
            if (stageConfig.getVerificationStageConfiguration() != null) {
                formConfiguration.getStages().add(new VerificationStage(VerificationStage
                        .VerificationStageConfiguration.builder()
                        .stageIsEnabled(stageConfig.getVerificationStageConfiguration().getStageIsEnabled())
                        .build()));
            }
            if (stageConfig.getPaymentStageConfiguration() != null) {
                formConfiguration.getStages().add(new PaymentStage(PaymentStage
                        .PaymentStageConfiguration.builder()
                            .stageIsEnabled(stageConfig.getPaymentStageConfiguration().getStageIsEnabled())
                        .amount(stageConfig.getPaymentStageConfiguration().getAmount())
                        .currency(stageConfig.getPaymentStageConfiguration().getCurrency())
                        .minimumAmountAllowed(stageConfig.getPaymentStageConfiguration().getMinimumAmountAllowed())
                        .description(stageConfig.getPaymentStageConfiguration().getDescription())
                        .build()));
            }
        }
        formConfiguration.getStages().add(new CompletedStage());
        return formConfiguration;
    }

    public Form getForm(String id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Form not found"));
    }

    public void deleteForm(String id) {
        formRepository.deleteById(id);
    }

}
