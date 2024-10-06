package shift.scheduler.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmployeeSettingsDTO(

        @Valid
        @NotNull(message = "Minimum and maximum hours per day must be set")
        TimeIntervalDTO hoursPerDayRange,

        @Valid
        @NotNull(message = "Minimum and maximum hours per week must be set")
        TimeIntervalDTO hoursPerWeekRange,

        @Valid
        List<AvailabilityDTO> availabilities
) {}
