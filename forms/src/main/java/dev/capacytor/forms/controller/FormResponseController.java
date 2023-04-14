package dev.capacytor.forms.controller;

import dev.capacytor.forms.model.CreateFormResponseDto;
import dev.capacytor.forms.service.FormResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController()
@RequestMapping("/form-response")
public class FormResponseController {
    private final FormResponseService formResponseService;

    @Autowired
    public FormResponseController(FormResponseService formResponseService) {
        this.formResponseService = formResponseService;
    }

    @PostMapping()
    public ResponseEntity<Object> createFormResponse(@RequestBody CreateFormResponseDto createFormResponseDto) {
        var formResponse = formResponseService.createFormResponse(createFormResponseDto);
        // TODO: return the correct URI
        return ResponseEntity.created(URI.create("http://localhost:9001/form-response/".concat(formResponse.getId())))
                .body(formResponse);
    }
}
