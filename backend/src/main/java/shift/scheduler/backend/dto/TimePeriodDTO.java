package shift.scheduler.backend.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.TimeInterval;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

@Interval
public record TimePeriodDTO(
        @Enumerated(EnumType.ORDINAL)
        Day day,

        @Hour
        @NotNull
        Short startHour,

        @Hour
        @NotNull
        Short endHour
) implements TimeInterval {

    @Override
    public boolean areBothEndsNonNull() {
        return (startHour != null && endHour != null);
    }

    @Override
    public int compareEnds() {
        return Integer.compare(endHour - startHour, 0);
    }
}
