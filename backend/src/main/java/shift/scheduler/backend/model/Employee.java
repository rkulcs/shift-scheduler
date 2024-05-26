package shift.scheduler.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.util.Period;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "employee")
public class Employee extends User {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Short minHoursPerDay;
    private Short maxHoursPerDay;
    private Short minHoursPerWeek;
    private Short maxHoursPerWeek;

    @OneToMany(mappedBy = "employee")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<Availability> availabilities;

    public Employee() {}

    public Employee(Account account) {
        super(account);
    }

    public Employee(Account account, Company company,
                    Short minHoursPerDay, Short maxHoursPerDay,
                    Short minHoursPerWeek, Short maxHoursPerWeek) {

        super(account);
        this.company = company;
        this.minHoursPerDay = minHoursPerDay;
        this.maxHoursPerDay = maxHoursPerDay;
        this.minHoursPerWeek = minHoursPerWeek;
        this.maxHoursPerWeek = maxHoursPerWeek;
    }

    public @NotNull Company getCompany() {
        return company;
    }

    public void setCompany(@NotNull Company company) {
        this.company = company;
    }

    public Short getMinHoursPerDay() {
        return minHoursPerDay;
    }

    public void setMinHoursPerDay(Short minHoursPerDay) {
        this.minHoursPerDay = minHoursPerDay;
    }

    public Short getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

    public void setMaxHoursPerDay(Short maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public Short getMinHoursPerWeek() {
        return minHoursPerWeek;
    }

    public void setMinHoursPerWeek(Short minHoursPerWeek) {
        this.minHoursPerWeek = minHoursPerWeek;
    }

    public Short getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }

    public void setMaxHoursPerWeek(Short maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }

    public Collection<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Collection<Availability> availabilities) {

        this.availabilities = availabilities;
        this.availabilities.forEach(availability -> availability.setEmployee(this));
    }

    /**
     * Generates a list of all possible shifts that the employee could work during the given time period.
     */
    public Collection<Shift> generatePotentialShifts(HoursOfOperation period) {

        Availability availability = availabilities
                .stream()
                .filter(a -> a.getDay().equals(period.getDay())).findFirst().orElse(null);

        if (availability == null)
            return null;

        Collection<Shift> potentialShifts = new ArrayList<>();

        for (short length = minHoursPerDay; length <= maxHoursPerDay; length += Period.HOURS) {
            for (short time = period.getStartHour(); time < period.getEndHour(); time += Period.HOURS) {
                if (time+length <= period.getEndHour())
                    potentialShifts.add(new Shift(time, (short) (time+length), this));
            }
        }

        return potentialShifts;
    }
}
