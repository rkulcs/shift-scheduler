package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.period.*;
import shift.scheduler.backend.util.Period;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "employee")
public class Employee extends User {

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Company company;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "daily_hours_id")
    private TimePeriod hoursPerDayRange = new TimePeriod((short) 0, (short) 4);

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weekly_hours_id")
    private TimePeriod hoursPerWeekRange = new TimePeriod((short) 0, (short) 4);

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<TimePeriod> availabilities;

    public Employee() {}

    public Employee(Account account) {
        super(account);
        this.availabilities = new ArrayList<>();
    }

    public Employee(Account account, Company company) {
        this(account);
        this.company = company;
    }

    public Employee(Account account, Company company,
                    Short minHoursPerDay, Short maxHoursPerDay,
                    Short minHoursPerWeek, Short maxHoursPerWeek) {

        super(account);
        this.company = company;
        this.hoursPerDayRange = new TimePeriod(minHoursPerDay, maxHoursPerDay);
        this.hoursPerWeekRange = new TimePeriod(minHoursPerWeek, maxHoursPerWeek);
        this.availabilities = new ArrayList<>();
    }

    public @NotNull Company getCompany() {
        return company;
    }

    public void setCompany(@NotNull Company company) {
        this.company = company;
    }

    public TimePeriod getHoursPerDayRange() {
        return hoursPerDayRange;
    }

    public TimePeriod getHoursPerWeekRange() {
        return hoursPerWeekRange;
    }

    @JsonIgnore
    public Short getMinHoursPerDay() {
        return hoursPerDayRange.getStartHour();
    }

    @JsonIgnore
    public Short getMaxHoursPerDay() {
        return hoursPerDayRange.getEndHour();
    }

    public void setHoursPerDayRange(Short min, Short max) {
        var range = (TimePeriod) hoursPerDayRange;
        range.setStartHour(min);
        range.setEndHour(max);
    }

    @JsonIgnore
    public Short getMinHoursPerWeek() {
        return hoursPerWeekRange.getStartHour();
    }

    @JsonIgnore
    public Short getMaxHoursPerWeek() {
        return hoursPerWeekRange.getEndHour();
    }

    public void setHoursPerWeekRange(Short min, Short max) {
        var range = (TimePeriod) hoursPerWeekRange;
        range.setStartHour(min);
        range.setEndHour(max);
    }

    public Collection<TimePeriod> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Collection<TimePeriod> availabilities) {

        if (this.availabilities == null) {
            this.availabilities = availabilities;
        } else {
            this.availabilities.clear();
            this.availabilities.addAll(availabilities);
        }
    }

    public TimePeriod getAvailabilityOn(Day day) {

        var availability = availabilities
                .stream()
                .filter(a -> a.getDay().equals(day)).findFirst().orElse(null);

        return availability;
    }

    /**
     * Determines if the employee is available to work on the given day.
     */
    public boolean isAvailableOn(Day day) {
        return (getAvailabilityOn(day) != null);
    }

    /**
     * Generates a list of all possible shifts that the employee could work during the given time period.
     */
    public Collection<Shift> generatePotentialShifts(TimePeriod period) {

        if (!isAvailableOn(period.getDay()))
            return null;

        Collection<Shift> potentialShifts = new ArrayList<>();

        for (short length = hoursPerDayRange.getStartHour(); length <= hoursPerDayRange.getEndHour(); length += Period.HOURS) {
            for (short time = period.getStartHour(); time < period.getEndHour(); time += Period.HOURS) {
                if (time+length <= period.getEndHour())
                    potentialShifts.add(new Shift(time, (short) (time+length), this));
            }
        }

        return potentialShifts;
    }
}
