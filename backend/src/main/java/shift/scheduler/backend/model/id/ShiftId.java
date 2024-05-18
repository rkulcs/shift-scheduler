package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Employee;

import java.io.Serializable;
import java.util.Calendar;

public class ShiftId implements Serializable {

    private Employee employee;
    private Calendar date;

    public ShiftId() {}

    public ShiftId(Employee employee, Calendar date) {
        this.employee = employee;
        this.date = date;
    }
}
