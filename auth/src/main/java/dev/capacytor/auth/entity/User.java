package dev.capacytor.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Transient
    public static final String ID_PREFIX = "u_";
    @Id
    private String id;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private List<String> authorities;
    private boolean enabled;
}
