package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.id.ScheduleId;
import shift.scheduler.backend.model.violation.ScheduleConstraintViolation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@IdClass(ScheduleId.class)
public class ScheduleForWeek {

    @Id
    @ManyToOne
    @JsonIncludeProperties("id")
    private Company company;

    @Id
    @Temporal(TemporalType.DATE)
    private LocalDate firstDay;

    @OneToMany(orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<ScheduleForDay> dailySchedules;

    public ScheduleForWeek() {}

    public ScheduleForWeek(Collection<ScheduleForDay> dailySchedules) {
        this.dailySchedules = dailySchedules;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

        if (this.dailySchedules == null) {
            this.dailySchedules = dailySchedules;
        } else {
            this.dailySchedules.clear();
            this.dailySchedules.addAll(dailySchedules);
        }
    }

    public List<ScheduleConstraintViolation> getConstraintViolations() {

        return Stream.of(this.dailySchedules)
                .flatMap(Collection::stream)
                .map(ScheduleForDay::getConstraintViolations)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
