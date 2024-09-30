package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.Employee;

import java.io.Serializable;

public class AvailabilityId implements Serializable {

    private Employee employee;

    private Day day;

    public AvailabilityId() {}

    public AvailabilityId(Employee employee, Day day) {
        this.employee = employee;
        this.day = day;
    }
}
