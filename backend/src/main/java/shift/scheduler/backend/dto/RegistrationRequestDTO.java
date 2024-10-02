package shift.scheduler.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequestDTO(

        @Valid
        @NotNull(message = "Account details must be set")
        AccountDTO account,

        @Valid
        @NotNull(message = "Company details must be set")
        CompanyDTO company
) {}
