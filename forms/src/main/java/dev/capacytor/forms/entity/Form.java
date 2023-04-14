package dev.capacytor.forms.entity;

import dev.capacytor.forms.commons.form.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "forms")
public class Form {
    @Id
    private String id;
    private String name;
    private String description;
    @Builder.Default
    private List<Section> sections = new ArrayList<>();
    private Configuration configuration;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Field {
        public enum FieldType {
            CHECKBOX, DATE, NUMBER, RADIO, SELECT, TEXT, TEXTAREA, TIME
        }

        private String id;
        private String name;
        private FieldType type;
        @Builder.Default
        private List<String> options = new ArrayList<>();
        @Builder.Default
        private Boolean isRequired = false;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Section {
        private String id;
        private String name;
        private String description;
        @Builder.Default
        private List<Field> fields = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Configuration {
        @Builder.Default
        private List<Stage> stages = new ArrayList<>();
    }


}
