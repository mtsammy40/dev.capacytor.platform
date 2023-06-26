package dev.capacytor.payments.repository;

import dev.capacytor.payments.entity.Client;
import dev.capacytor.payments.entity.MethodConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
