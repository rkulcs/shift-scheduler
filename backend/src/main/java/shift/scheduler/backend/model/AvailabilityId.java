package shift.scheduler.backend.model;

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
