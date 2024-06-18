package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.model.id.ShiftId;

import java.time.LocalDate;

@Entity
@IdClass(ShiftId.class)
public class Shift extends TimePeriod {

    @Id
    @ManyToOne
    private Employee employee;

    @Id
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    public Shift() {
        super();
    }

    public Shift(Short startHour, Short endHour, Employee employee) {
        super(startHour, endHour);
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
