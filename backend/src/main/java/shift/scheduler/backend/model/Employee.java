package shift.scheduler.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Employee extends User {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Short minHoursPerDay;
    private Short maxHoursPerDay;
    private Short minHoursPerWeek;
    private Short maxHoursPerWeek;

    @OneToMany
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
    }
}
