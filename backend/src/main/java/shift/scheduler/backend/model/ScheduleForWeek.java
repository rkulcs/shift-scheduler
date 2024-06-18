package shift.scheduler.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.Collection;

@Entity
public class ScheduleForWeek {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate firstDay;

    @OneToMany(orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<ScheduleForDay> dailySchedules;

    public ScheduleForWeek() {}

    public ScheduleForWeek(Collection<ScheduleForDay> dailySchedules) {
        this.dailySchedules = dailySchedules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    public Collection<ScheduleForDay> getDailySchedules() {
        return dailySchedules;
    }

    public void setDailySchedules(Collection<ScheduleForDay> dailySchedules) {
        this.dailySchedules = dailySchedules;
    }
}
