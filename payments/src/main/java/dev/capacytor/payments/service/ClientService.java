package dev.capacytor.payments.service;

import dev.capacytor.payments.entity.Client;
import dev.capacytor.payments.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client getClient(String clientId) {
        return clientRepository.findById(UUID.fromString(clientId))
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
    }
}
