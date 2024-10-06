package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.util.validator.Hour;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TimePeriod implements TimeInterval {

    @Id
    private Long id;

    @Hour
    @NotNull
    @JsonView(EntityViews.Associate.class)
    private Short start;

    @Hour
    @NotNull
    @JsonView(EntityViews.Associate.class)
    private Short end;

    public TimePeriod() {}

    public TimePeriod(Short start, Short end) {
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getStart() {
        return start;
    }

    public void setStart(Short start) {
        this.start = start;
    }

    public Short getEnd() {
        return end;
    }

    public void setEnd(@NotNull Short end) {
        this.end = end;
    }

    @JsonIgnore
    public int getLength() {
        return end - start;
    }

    public boolean contains(TimePeriod interval) {
        return (start <= interval.getStart() && interval.getEnd() <= end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
