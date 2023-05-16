package dev.capacytor.payments.repository;

import dev.capacytor.payments.entity.MethodConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MethodRepository extends JpaRepository<MethodConfig, String> {
    List<MethodConfig> findAllByStatus(MethodConfig.Status status);
}
