package shift.scheduler.backend.model.schedule;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.id.ScheduleId;
import shift.scheduler.backend.model.violation.EmployeeConstraintViolation;
import shift.scheduler.backend.model.violation.ScheduleConstraintViolation;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Entity
@IdClass(ScheduleId.class)
public class ScheduleForWeek extends Schedule {

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

    public Collection<ScheduleConstraintViolation> getConstraintViolations() {

        Stream<ScheduleConstraintViolation> dailyScheduleViolations = Stream.of(this.dailySchedules)
                .flatMap(Collection::stream)
                .map(ScheduleForDay::getConstraintViolations)
                .flatMap(Collection::stream);

        return Stream.concat(this.constraintViolations.stream(), dailyScheduleViolations).toList();
    }

    public boolean validate() {

        constraintViolations = new ArrayList<>();
        Map<Employee, Integer> employeeWeeklyHours = new HashMap<>();

        for (var dailySchedule : dailySchedules) {
            dailySchedule.getEmployeeHours().forEach((employee, hours) -> {
                if (employeeWeeklyHours.containsKey(employee))
                    employeeWeeklyHours.put(employee, employeeWeeklyHours.get(employee)+hours);
                else
                    employeeWeeklyHours.put(employee, 0);
            });
        }

        employeeWeeklyHours.forEach((employee, hours) -> {
            int difference = 0;

            if (employee.getMaxHoursPerWeek() < hours)
                difference = hours - employee.getMaxHoursPerWeek();
            else if (hours < employee.getMinHoursPerWeek())
                difference = employee.getMinHoursPerWeek() - hours;

            if (difference != 0) {
                constraintViolations.add(new EmployeeConstraintViolation(
                        employee, EmployeeConstraintViolation.Type.WEEKLY_HOURS, difference
                ));
            }
        });

        return getConstraintViolations().isEmpty();
    }
}
