package shift.scheduler.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import shift.scheduler.backend.model.Role;

public record AccountDTO(
        @NotEmpty(message = "Name must be set")
        String name,

        @NotEmpty(message = "Username must be set")
        String username,

        @NotEmpty(message = "Password must be set")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @Valid
        @NotNull(message = "Role must be set")
        Role role
) {}
