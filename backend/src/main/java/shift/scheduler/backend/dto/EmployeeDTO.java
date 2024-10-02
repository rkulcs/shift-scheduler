package shift.scheduler.backend.dto;

import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.Company;

public record EmployeeDTO(
        Long id,

        @NotNull(message = "Account cannot be null")
        AccountDTO account
) {
}
