package dev.capacytor.forms.repository;

import dev.capacytor.forms.entity.Form;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormRepository extends MongoRepository<Form, String> {
}
