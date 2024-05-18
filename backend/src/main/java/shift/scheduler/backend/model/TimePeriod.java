package shift.scheduler.backend.model;

import jakarta.persistence.*;

@MappedSuperclass
public class TimePeriod {

    private Short startHour;

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
