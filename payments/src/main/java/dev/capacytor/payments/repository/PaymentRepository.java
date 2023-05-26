package dev.capacytor.payments.repository;

import dev.capacytor.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findPaymentByProviderReference(String providerReference);
}
