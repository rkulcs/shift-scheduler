package shift.scheduler.backend.model.violation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import shift.scheduler.backend.model.Employee;

public class EmployeeConstraintViolation extends ScheduleConstraintViolation {

    public enum Type {
        DAILY_HOURS,
        WEEKLY_HOURS
    }

    @JsonIgnore
    private Employee employee;

    @JsonIgnore
    private Type type;

    public EmployeeConstraintViolation(Employee employee, Type type, int difference) {
        this.employee = employee;
        this.type = type;
        this.difference = difference;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append(employee.getName());
        builder.append("'s ");

        if (difference < 0)
            builder.append("minimum ");
        else
            builder.append("maximum ");

        switch (type) {
            case DAILY_HOURS -> builder.append("daily hours ");
            case WEEKLY_HOURS -> builder.append("weekly hours ");
        }

        if (difference < 0)
            builder.append("subceeded by ");
        else
            builder.append("exceeded by ");

        builder.append(Math.abs(difference));
        builder.append(" hours.");

        return builder.toString();
    }
}
