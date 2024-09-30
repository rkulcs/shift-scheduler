package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.util.validator.Hour;
import shift.scheduler.backend.util.validator.Interval;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(startHour, that.startHour) && Objects.equals(endHour, that.endHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startHour, endHour);
    }
}
