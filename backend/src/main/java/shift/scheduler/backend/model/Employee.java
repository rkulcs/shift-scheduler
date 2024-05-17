package shift.scheduler.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    private Short minHoursPerDay;
    private Short maxHoursPerDay;
    private Short minHoursPerWeek;
    private Short maxHoursPerWeek;

    public Employee() {
        super();
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
