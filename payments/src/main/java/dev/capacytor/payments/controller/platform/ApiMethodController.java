package dev.capacytor.payments.controller.platform;

import dev.capacytor.payments.commons.Constants;
import dev.capacytor.payments.entity.MethodConfig;
import dev.capacytor.payments.model.CreateMethodConfigRequest;
import dev.capacytor.payments.service.MethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.Endpoints.PLATFORM_API_V1_METHODS)
@RequiredArgsConstructor
@Slf4j
public class ApiMethodController {

    private final MethodService methodService;

    @PostMapping("")
    ResponseEntity<MethodConfig> create(@RequestBody() CreateMethodConfigRequest request) {
        return ResponseEntity.ok(methodService.create(request));
    }

    @GetMapping("")
    ResponseEntity<List<MethodConfig>> list() {
        return ResponseEntity.ok(methodService.findAll());
    }
}
