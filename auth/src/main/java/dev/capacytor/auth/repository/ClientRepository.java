package dev.capacytor.auth.repository;

import dev.capacytor.auth.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
