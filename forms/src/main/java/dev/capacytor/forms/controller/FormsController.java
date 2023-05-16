package dev.capacytor.forms.controller;

import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.model.CreateFormDto;
import dev.capacytor.forms.model.CreateFormResponseDto;
import dev.capacytor.forms.service.FormResponseService;
import dev.capacytor.forms.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController()
@RequestMapping("/form")
@Slf4j
public class FormsController {

    private final FormService formService;
    private final FormResponseService formResponseService;

    @Autowired
    public FormsController(FormService formService, FormResponseService formResponseService) {
        this.formService = formService;
        this.formResponseService = formResponseService;
    }

    @PostMapping()
    public ResponseEntity<Object> createForm(@RequestBody CreateFormDto createFormDto) {
        var form = formService.createForm(createFormDto);
        // TODO: return the correct URI
        return ResponseEntity.created(URI.create("http://localhost:9001/form/".concat(form.getId())))
                .body(form);
    }

    @PostMapping("/{formId}/response")
    public ResponseEntity<Object> createResponse(@PathVariable String formId, @RequestBody CreateFormResponseDto createFormResponseDto) {
        var formResponse = formResponseService.createFormResponse(formId, createFormResponseDto);
        // TODO: return the correct URI
        return ResponseEntity.created(URI.create("http://localhost:9001/form/response/".concat(formResponse.getId())))
                .body(formResponse);
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
