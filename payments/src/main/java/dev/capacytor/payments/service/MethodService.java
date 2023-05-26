package dev.capacytor.payments.service;

import dev.capacytor.payments.entity.MethodConfig;
import dev.capacytor.payments.model.CreateMethodConfigRequest;
import dev.capacytor.payments.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MethodService {
    private final MethodRepository methodRepository;

    List<MethodConfig> findByStatus(MethodConfig.Status status) {
        return methodRepository.findAllByStatus(status);
    }

    public MethodConfig create(CreateMethodConfigRequest request) {
        log.info("Creating method config {}", request);

        var methodConfig = MethodConfig.builder()
                .name(request.name())
                .description(request.description())
                .status(MethodConfig.Status.ACTIVE)
                .type(request.type())
                .integrationConfig(request.integrationConfig())
                .operationConfig(request.operationConfig())
                .build();

        return methodRepository.save(methodConfig);

    }

    public MethodConfig update(String id, CreateMethodConfigRequest request) {
        var methodConfig = methodRepository.findById(id).orElseThrow();
        methodConfig.setName(request.name());
        methodConfig.setDescription(request.description());
        methodConfig.setType(request.type());
        methodConfig.setIntegrationConfig(request.integrationConfig());
        methodConfig.setOperationConfig(request.operationConfig());
        return methodRepository.save(methodConfig);
    }

    public MethodConfig findById(String id) {
        return methodRepository.findById(id).orElseThrow();
    }

    public List<MethodConfig> findAll() {
        return methodRepository.findAll();
    }
}
