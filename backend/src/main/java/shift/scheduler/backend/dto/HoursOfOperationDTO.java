package shift.scheduler.backend.dto;

import jakarta.validation.Valid;

import java.util.Collection;

public record HoursOfOperationDTO(
    Collection<@Valid TimePeriodDTO> timePeriods
) { }
