package shift.scheduler.backend.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    MANAGER("MANAGER"),
    EMPLOYEE("EMPLOYEE");

    private final String text;

    Role(final String text) {
        this.text = text;
    }

    @Override
    public String getAuthority() {
        return text;
    }
}
