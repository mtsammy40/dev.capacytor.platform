package dev.capacytor.forms.controller;

import dev.capacytor.forms.model.CreateFormDto;
import dev.capacytor.forms.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/form")
@Slf4j
public class FormsController {

    private final FormService formService;


    @Autowired
    public FormsController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping()
    public ResponseEntity<Object> createForm(@RequestBody CreateFormDto createFormDto) {
        var form = formService.createForm(createFormDto);
        // TODO: return the correct URI
        return ResponseEntity.created(URI.create("http://localhost:9001/form/".concat(form.getId())))
                .body(form);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getForm(@PathVariable String id) {
        return ResponseEntity.ok(formService.getForm(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> getForms() {
        return ResponseEntity.ok(formService.getForms());
    }
}
