package shift.scheduler.backend.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class ScheduleForDay {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Day day;

    @OneToMany
    private Collection<Shift> shifts;

    public ScheduleForDay() {}

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

    public Collection<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Collection<Shift> shifts) {
        this.shifts = shifts;
    }
}
