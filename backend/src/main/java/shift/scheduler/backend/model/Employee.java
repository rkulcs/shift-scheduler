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
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @Valid
    @NotNull
    @OneToOne
    private TimePeriod hoursPerDayRange;

    @Valid
    @NotNull
    @OneToOne
    private TimePeriod hoursPerWeekRange;

    @OneToMany(mappedBy = "employee", orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<Availability> availabilities;

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

    public Short getMinHoursPerDay() {
        return hoursPerDayRange.getStart();
    }

    public Short getMaxHoursPerDay() {
        return hoursPerDayRange.getEnd();
    }

    public void setHoursPerDayRange(Short min, Short max) {
        var range = (TimePeriod) hoursPerDayRange;
        range.setStart(min);
        range.setEnd(max);
    }

    public Short getMinHoursPerWeek() {
        return hoursPerWeekRange.getStart();
    }

    public Short getMaxHoursPerWeek() {
        return hoursPerWeekRange.getEnd();
    }

    public void setHoursPerWeekRange(Short min, Short max) {
        var range = (TimePeriod) hoursPerWeekRange;
        range.setStart(min);
        range.setEnd(max);
    }

    public Collection<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Collection<Availability> availabilities) {

        if (this.availabilities == null) {
            this.availabilities = availabilities;
        } else {
            this.availabilities.forEach(availability -> availability.setEmployee(null));
            this.availabilities.clear();
            this.availabilities.addAll(availabilities);
        }

        this.availabilities.forEach(availability -> availability.setEmployee(this));
    }

    public Availability getAvailabilityOn(Day day) {

        Availability availability = availabilities
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
    public Collection<Shift> generatePotentialShifts(HoursOfOperation period) {

        if (!isAvailableOn(period.getDay()))
            return null;

        Collection<Shift> potentialShifts = new ArrayList<>();

        for (short length = hoursPerDayRange.getStart(); length <= hoursPerDayRange.getEnd(); length += Period.HOURS) {
            for (short time = period.getStart(); time < period.getEnd(); time += Period.HOURS) {
                if (time+length <= period.getEnd())
                    potentialShifts.add(new Shift(time, (short) (time+length), this));
            }
        }

        return potentialShifts;
    }
}
