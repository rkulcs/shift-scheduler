package shift.scheduler.backend.model.period;

public interface TimeInterval {
    boolean areBothEndsNonNull();

    /* Should return -1 if the start value is lower than the end value, 0 if the
       start and end values are equal, and 1 if the end value is greater than the start value. */
    int compareEnds();
}
