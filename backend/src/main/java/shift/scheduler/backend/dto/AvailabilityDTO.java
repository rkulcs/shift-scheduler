package shift.scheduler.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.TimeInterval;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

@Interval
public record AvailabilityDTO(
        @Valid
        @NotNull(message = "Day must be set")
        Day day,

        @Hour
        @Max(24)
        @NotNull(message = "Start hour must be set")
        Short startHour,

        @Hour
        @Max(24)
        @NotNull(message = "End hour must be set")
        Short endHour
) implements TimeInterval {

        @Override
        public Short getStart() {
                return startHour;
        }

        @Override
        public Short getEnd() {
                return endHour;
        }
}
