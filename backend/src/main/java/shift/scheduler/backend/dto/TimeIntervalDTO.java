package shift.scheduler.backend.dto;

import shift.scheduler.backend.model.period.TimeInterval;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

@Interval
public record TimeIntervalDTO(
        @Hour
        Short start,

        @Hour
        Short end
) implements TimeInterval {
    @Override
    public Short getStart() {
        return start;
    }

    @Override
    public Short getEnd() {
        return end;
    }
}
