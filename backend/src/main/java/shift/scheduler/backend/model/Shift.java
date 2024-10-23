package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import shift.scheduler.backend.model.period.TimePeriod;

@Entity
@Table(name = "shift")
public class Shift {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Employee employee;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private TimePeriod timePeriod;

    public Shift() {
        super();
    }

    public Shift(Short startHour, Short endHour, Employee employee) {
        this.timePeriod = new TimePeriod(startHour, endHour);
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(@NotNull TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    @JsonIgnore
    public Short getStart() {
        return timePeriod.getStartHour();
    }

    @JsonIgnore
    public Short getEnd() {
        return timePeriod.getEndHour();
    }

    @JsonIgnore
    public int getLength() {
        return timePeriod.getLength();
    }

    public boolean contains(TimePeriod interval) {
        return timePeriod.contains(interval);
    }
}
