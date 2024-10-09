package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.model.period.TimePeriod;

@Entity
@Table(name = "shift")
public class Shift {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Employee employee;

    @OneToOne
    private TimePeriod timePeriod;

    public Shift() {
        super();
    }

    public Shift(Short startHour, Short endHour, Employee employee) {
        this.timePeriod = new TimePeriod(startHour, endHour);
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Short getStart() {
        return timePeriod.getStartHour();
    }

    public Short getEnd() {
        return timePeriod.getEndHour();
    }

    public int getLength() {
        return timePeriod.getLength();
    }

    public boolean contains(TimePeriod interval) {
        return timePeriod.contains(interval);
    }
}
