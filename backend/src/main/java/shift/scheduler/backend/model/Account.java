package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Account {

    @Transient
    private static final short MIN_PASSWORD_LENGTH = 8;

    @Transient
    private static final short MAX_PASSWORD_LENGTH = 255;

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

    public Account() {}

    public Account(String username, String name, String password) {
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
        return (password != null && MIN_PASSWORD_LENGTH <= password.length()
                && password.length() <= MAX_PASSWORD_LENGTH);
    }
}
