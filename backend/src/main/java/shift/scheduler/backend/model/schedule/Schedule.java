package shift.scheduler.backend.model.schedule;

import jakarta.persistence.Transient;
import shift.scheduler.backend.model.violation.ScheduleConstraintViolation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Schedule {

    @Transient
    Collection<ScheduleConstraintViolation> constraintViolations = new ArrayList<>();

    public abstract Collection<ScheduleConstraintViolation> getConstraintViolations();
    public abstract boolean validate();
}
