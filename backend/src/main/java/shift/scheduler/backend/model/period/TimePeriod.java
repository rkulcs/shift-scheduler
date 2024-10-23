package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.util.validator.Hour;

import java.util.Objects;

@Entity
public class TimePeriod {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Day day;

    @Hour
    @NotNull
    @JsonView(EntityViews.Associate.class)
    private Short startHour;

    @Hour
    @NotNull
    @JsonView(EntityViews.Associate.class)
    private Short endHour;

    public TimePeriod() {}

    public TimePeriod(Short startHour, Short endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public TimePeriod(Day day, Short startHour, Short endHour) {
        this(startHour, endHour);
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
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
        return endHour - startHour;
    }

    @JsonIgnore
    public boolean contains(TimePeriod interval) {
        return (startHour <= interval.getStartHour() && interval.getEndHour() <= endHour);
    }

    @Override
    @JsonIgnore
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(startHour, that.startHour) && Objects.equals(endHour, that.endHour);
    }

    @Override
    @JsonIgnore
    public int hashCode() {
        return Objects.hash(startHour, endHour);
    }
}
