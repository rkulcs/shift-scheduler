package shift.scheduler.backend.dto;

import jakarta.validation.constraints.NotEmpty;

public record CompanyDTO(
        @NotEmpty(message = "Company name must be set")
        String name,

        @NotEmpty(message = "Company location must be set")
        String location
) {}
