package shift.scheduler.backend.dto;

import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.period.TimeInterval;
import shift.scheduler.backend.util.validator.Hour;

public record TimeIntervalDTO(
        @NotNull(message = "Start must be set")
        @Hour
        Short start,

        @NotNull(message = "End must be set")
        @Hour
        Short end
) implements TimeInterval {
    @Override
    public boolean areBothEndsNonNull() {
        return (start != null && end != null);
    }

    @Override
    public int compareEnds() {
        return Integer.compare(end - start, 0);
    }
}
