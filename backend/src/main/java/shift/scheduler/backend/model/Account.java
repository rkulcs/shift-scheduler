package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class Account implements UserDetails {

    @Id
    @NotNull
    @NotBlank
    private String username;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @JsonIgnore
    private Role role;

    public Account() {}

    public Account(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public Account(String username, String name, String password, Role role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
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

    public @NotNull Role getRole() {
        return role;
    }

    public void setRole(@NotNull Role role) {
        this.role = role;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
    }
}
