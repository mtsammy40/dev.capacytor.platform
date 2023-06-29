package dev.capacytor.forms.controller;

import dev.capacytor.forms.commons.Constants;
import dev.capacytor.forms.configuration.CapacytorNetworkProperties;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.mapper.FormResponseMapper;
import dev.capacytor.forms.model.CreateFormDto;
import dev.capacytor.forms.model.CreateFormResponseDto;
import dev.capacytor.forms.model.FormResponseDto;
import dev.capacytor.forms.service.FormResponseService;
import dev.capacytor.forms.service.FormService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController()
@RequestMapping(Constants.Endpoints.FORMS)
@Slf4j
public class FormsController {

    private final FormService formService;
    private final FormResponseService formResponseService;
    private final FormResponseMapper formResponseMapper;
    private final CapacytorNetworkProperties capacytorNetworkProperties;

    @PostConstruct
    void init() {
        log.info("FormsController initialized...");
        this.formResponseMapper.withProperties(capacytorNetworkProperties);
    }

    @Autowired
    public FormsController(FormService formService, FormResponseService formResponseService, FormResponseMapper formResponseMapper, CapacytorNetworkProperties capacytorNetworkProperties) {
        this.formService = formService;
        this.formResponseService = formResponseService;
        this.formResponseMapper = formResponseMapper;
        this.capacytorNetworkProperties = capacytorNetworkProperties;
    }

    @PostMapping()
    public ResponseEntity<Object> createForm(@RequestBody CreateFormDto createFormDto) {
        var form = formService.createForm(createFormDto);
        return ResponseEntity.created(
                        URI.create(capacytorNetworkProperties
                                .getBase()
                                .concat(Constants.Endpoints.FORMS)
                                .concat(form.getId())))
                .body(form);
    }

    @PostMapping("/{formId}/response")
    public ResponseEntity<FormResponseDto> createResponse(@PathVariable String formId, @RequestBody CreateFormResponseDto createFormResponseDto) {
        var formResponse = formResponseService.createFormResponse(formId, createFormResponseDto);
        return ResponseEntity.created(URI.create(capacytorNetworkProperties
                        .getBase()
                        .concat("/form/response/".concat(formResponse.getId()))))
                .body(formResponseMapper.formResponseToFormResponseDto(formResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getForm(@PathVariable String id) {
        return ResponseEntity.ok(formService.getForm(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteForm(@PathVariable String id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/response/{responseId}")
    public ResponseEntity<Object> getResponse(@PathVariable String responseId) {
        return ResponseEntity.ok(formResponseService.getFormResponseById(responseId));
    }

    @GetMapping("/{formId}/response")
    public ResponseEntity<List<FormResponse>> getResponses(@PathVariable String formId) {
        return ResponseEntity.ok(formResponseService.getFormResponsesByFormId(formId));
    }

    @GetMapping("")
    public ResponseEntity<Object> getForms() {
        return ResponseEntity.ok(formService.getForms());
    }
}
