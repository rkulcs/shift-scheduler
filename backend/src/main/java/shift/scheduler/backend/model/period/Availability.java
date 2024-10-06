package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import shift.scheduler.backend.model.Employee;

@Entity
@Table(name = "availability")
public class Availability extends TimePeriod {

    @ManyToOne
    @JsonIgnore
    private Employee employee;

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

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
