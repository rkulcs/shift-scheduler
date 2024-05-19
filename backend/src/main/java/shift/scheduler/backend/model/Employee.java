package shift.scheduler.backend.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Employee extends User {

    private Short minHoursPerDay;
    private Short maxHoursPerDay;
    private Short minHoursPerWeek;
    private Short maxHoursPerWeek;

    @OneToMany
    private Collection<Availability> availabilities;

    public Employee() {
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
}
