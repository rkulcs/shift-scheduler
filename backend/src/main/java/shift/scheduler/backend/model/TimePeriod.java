package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

@MappedSuperclass
@Interval
public class TimePeriod {

    @Hour
    private Short startHour;

    @Hour
    private Short endHour;

    public TimePeriod() {}

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
