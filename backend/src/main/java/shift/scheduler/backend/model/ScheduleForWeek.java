package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Collection;

@Entity
public class ScheduleForWeek {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private Collection<ScheduleForDay> dailySchedules;

    public ScheduleForWeek() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<ScheduleForDay> getDailySchedules() {
        return dailySchedules;
    }

    public void setDailySchedules(Collection<ScheduleForDay> dailySchedules) {
        this.dailySchedules = dailySchedules;
    }
}
