package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Employee;

import java.io.Serializable;
import java.time.LocalDate;

public class ShiftId implements Serializable {

    private Employee employee;
    private LocalDate date;

    public ShiftId() {}

    public ShiftId(Employee employee, LocalDate date) {
        this.employee = employee;
        this.date = date;
    }
}
