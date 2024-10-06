package shift.scheduler.backend.model.period;

import shift.scheduler.backend.util.validator.Interval;

@Interval
public interface TimeInterval {
    Short getStart();
    Short getEnd();
}
