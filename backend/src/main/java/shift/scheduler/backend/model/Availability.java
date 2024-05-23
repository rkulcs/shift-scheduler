package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.model.id.AvailabilityId;

@Entity
@Table(name = "availability")
@IdClass(AvailabilityId.class)
public class Availability extends TimePeriod {

    @Id
    @ManyToOne
    private Employee employee;

    @Id
    @Enumerated(EnumType.ORDINAL)
    private Day day;

    public Availability() {}

    public Availability(Short startHour, Short endHour, Day day) {
        super(startHour, endHour);
        this.day = day;
    }

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
