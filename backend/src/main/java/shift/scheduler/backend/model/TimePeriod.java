package shift.scheduler.backend.model;

import jakarta.persistence.*;

@MappedSuperclass
public class TimePeriod {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Employee employee;

    private Short startHour;

    private Short endHour;

    public TimePeriod() {}

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

    public Short getStartHour() {
        return startHour;
    }

    public void setStartHour(Short startHour) {
        this.startHour = startHour;
    }

    public Short getEndHour() {
        return endHour;
    }

    public void setEndHour(Short endHour) {
        this.endHour = endHour;
    }
}
