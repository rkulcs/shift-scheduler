package shift.scheduler.backend.model.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.model.violation.CompanyConstraintViolation;
import shift.scheduler.backend.model.violation.EmployeeConstraintViolation;
import shift.scheduler.backend.model.violation.ScheduleConstraintViolation;
import shift.scheduler.backend.util.Period;

import java.util.*;

@Entity
public class ScheduleForDay extends Schedule {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Day day;

    @OneToMany(orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<Shift> shifts;

    @Transient
    @JsonIgnore
    private Map<Employee, Integer> employeeHours;

    @Transient
    @JsonIgnore
    private Map<TimePeriod, Integer> numEmployeesPerPeriod;

    @Transient
    private int numEmployeesPerHour;

    public ScheduleForDay() {}

    public ScheduleForDay(Day day, Collection<Shift> shifts) {
        this.day = day;
        this.shifts = shifts;
        this.computeEmployeeHours();
    }

    public ScheduleForDay(Day day, Collection<Shift> shifts, List<TimePeriod> blocks, int numEmployeesPerHour) {
        this(day, shifts);
        this.numEmployeesPerHour = numEmployeesPerHour;
        this.computeNumEmployeesPerPeriod(blocks);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Collection<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Collection<Shift> shifts) {
        this.shifts = shifts;
    }

    public Collection<ScheduleConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    public Map<Employee, Integer> getEmployeeHours() {
        return employeeHours;
    }

    public Map<TimePeriod, Integer> getNumEmployeesPerPeriod() {
        return numEmployeesPerPeriod;
    }

    /**
     * Checks if the schedule meets all constraints, and stores all constraint violations in
     * a list.
     *
     * @return True if no constraints are violated, false otherwise
     */
    public boolean validate() {

        constraintViolations = new ArrayList<>();

        for (var entry : employeeHours.entrySet()) {
            Employee employee = entry.getKey();
            int hours = entry.getValue();

            int difference = 0;

            if (hours < employee.getMinHoursPerDay())
                difference = employee.getMinHoursPerDay() - hours;
            else if (hours > employee.getMaxHoursPerDay())
                difference = hours - employee.getMaxHoursPerDay();

            if (difference != 0) {
                constraintViolations.add(new EmployeeConstraintViolation(
                        employee, EmployeeConstraintViolation.Type.DAILY_HOURS, difference
                ));
            }
        }

        for (var entry : numEmployeesPerPeriod.entrySet()) {
            var period = entry.getKey();
            int employeeCount = entry.getValue();

            int difference = 0;

            if (employeeCount > numEmployeesPerHour)
                difference = employeeCount - numEmployeesPerHour;
            else if (employeeCount < numEmployeesPerHour)
                difference = employeeCount - numEmployeesPerHour;

            if (difference != 0)
                constraintViolations.add(new CompanyConstraintViolation(period, difference));
        }

        return constraintViolations.isEmpty();
    }

    /**
     * Computes the number of hours worked by each employee who is scheduled to
     * work, and stores the results in a map.
     */
    private void computeEmployeeHours() {

        employeeHours = new HashMap<>();

        for (var shift : shifts) {
            Employee employee = shift.getEmployee();
            int length = shift.getLength();

            if (employeeHours.containsKey(employee))
                employeeHours.put(employee, employeeHours.get(employee)+length);
            else
                employeeHours.put(employee, length);
        }
    }

    /**
     * Computes the number of employees during each period of the day's hours of operation.
     *
     * @param blocks The time periods that the operational day consists of (e.g., 4-8, 8-12, 12-16 if the hours of
     *               operation are 4-16, and a period consists of four hours)
     */
    private void computeNumEmployeesPerPeriod(List<TimePeriod> blocks) {

        numEmployeesPerPeriod = new HashMap<>();

        Map<Short, TimePeriod> startHourToPeriodMap = new HashMap<>();

        for (TimePeriod block : blocks) {
            numEmployeesPerPeriod.put(block, 0);
            startHourToPeriodMap.put(block.getStart(), block);
        }

        for (var shift : shifts) {
            for (short hour = shift.getStart(); hour < shift.getEnd(); hour += Period.HOURS) {
                TimePeriod block = startHourToPeriodMap.get(hour);
                numEmployeesPerPeriod.put(block, numEmployeesPerPeriod.get(block)+1);
            }
        }
    }
}
