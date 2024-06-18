package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Employee;

import java.io.Serializable;
import java.util.Date;

public class ShiftId implements Serializable {

    private Employee employee;
    private Date date;

    public ShiftId() {}

    public ShiftId(Employee employee, Date date) {
        this.employee = employee;
        this.date = date;
    }
}
