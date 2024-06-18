package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Shift extends TimePeriod {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Employee employee;

    public Shift() {
        super();
    }

    public Shift(Short startHour, Short endHour, Employee employee) {
        super(startHour, endHour);
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
}
