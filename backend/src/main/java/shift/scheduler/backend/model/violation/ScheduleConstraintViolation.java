package shift.scheduler.backend.model.violation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ScheduleConstraintViolation {

    @JsonIgnore
    protected int difference;

    public int getDifference() {
        return difference;
    }

    @Override
    @JsonProperty("description")
    public abstract String toString();
}
