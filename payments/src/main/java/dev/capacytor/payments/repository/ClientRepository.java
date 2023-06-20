package dev.capacytor.payments.repository;

import dev.capacytor.payments.entity.Client;
import dev.capacytor.payments.entity.MethodConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
}
