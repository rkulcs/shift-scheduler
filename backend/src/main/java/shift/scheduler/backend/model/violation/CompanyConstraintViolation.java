package shift.scheduler.backend.model.violation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import shift.scheduler.backend.model.period.TimePeriod;

public class CompanyConstraintViolation extends ScheduleConstraintViolation {

    @JsonIgnore
    private TimePeriod period;

    public CompanyConstraintViolation(TimePeriod period, int difference) {
        this.period = period;
        this.difference = difference;
    }

    public TimePeriod getPeriod() {
        return period;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append(String.format(
                "Required number of employees between %d:00 and %d:00 ",
                period.getStartHour(), period.getEndHour()
        ));

        if (difference < 0)
            builder.append("subceeded ");
        else
            builder.append("exceeded ");

        builder.append(String.format("by %d.", Math.abs(difference)));

        return builder.toString();
    }
}
