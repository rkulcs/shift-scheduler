package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

@MappedSuperclass
@Interval
public class TimePeriod {

    @Hour
    @JsonView(EntityViews.Associate.class)
    private Short startHour;

    @Hour
    @JsonView(EntityViews.Associate.class)
    private Short endHour;

    public TimePeriod() {}

    public TimePeriod(Short startHour, Short endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
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

    @JsonIgnore
    public int getLength() {
        return endHour-startHour;
    }

    public boolean contains(TimePeriod period) {
        return (startHour <= period.getStartHour() && period.getEndHour() <= endHour);
    }
}
