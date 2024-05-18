package shift.scheduler.backend.model;

import jakarta.persistence.*;

@Entity
@IdClass(AvailabilityId.class)
public class Availability extends TimePeriod {

    public enum Day {
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN
    }

    @Id
    @ManyToOne
    private Employee employee;

    @Id
    @Enumerated(EnumType.ORDINAL)
    private Availability.Day day;

    public Availability() {}

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Availability.Day getDay() {
        return day;
    }

    public void setDay(Availability.Day day) {
        this.day = day;
    }
}
