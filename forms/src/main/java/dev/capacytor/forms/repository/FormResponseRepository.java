package dev.capacytor.forms.repository;

import dev.capacytor.forms.entity.FormResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormResponseRepository extends MongoRepository<FormResponse, String> {
}
