package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Availability;
import shift.scheduler.backend.model.Employee;

import java.io.Serializable;

public class AvailabilityId implements Serializable {

    private Employee employee;

    private Availability.Day day;

    public AvailabilityId() {}

    public AvailabilityId(Employee employee, Availability.Day day) {
        this.employee = employee;
        this.day = day;
    }
}
