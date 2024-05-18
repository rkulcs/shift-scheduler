package shift.scheduler.backend.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public class User {

    @Id
    @NotNull
    @NotBlank
    private String username;

    @NotBlank
    @NotNull
    private String name;

    @Transient
    private String password;

    @NotBlank
    @NotNull
    private String passwordHash;

    public User() {}

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public static boolean validatePassword(String password) {
        return (password != null && 8 <= password.length() && password.length() <= 200);
    }
}
