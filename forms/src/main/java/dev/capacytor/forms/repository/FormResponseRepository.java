package dev.capacytor.forms.repository;

import dev.capacytor.forms.entity.FormResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FormResponseRepository extends MongoRepository<FormResponse, String> {
    List<FormResponse> findAllByFormId(String formId);
}
