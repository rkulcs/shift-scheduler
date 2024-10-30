package shift.scheduler.backend.util.builder;

import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.schedule.ScheduleForDay;

import java.util.HashMap;
import java.util.Map;

public class ScheduleForDayBuilder implements Builder<ScheduleForDay> {

    private Day day;

    private final Map<Employee, Shift> shifts = new HashMap<>();

    public ScheduleForDayBuilder setDay(Day day) {
        this.day = day;
        return this;
    }

    public ScheduleForDayBuilder addShift(Employee employee, int start, int end) {
        shifts.put(employee, new Shift((short) start, (short) end, employee));
        return this;
    }

    public ScheduleForDayBuilder addShift(Shift shift) {
        shifts.put(shift.getEmployee(), shift);
        return this;
    }

    @Override
    public ScheduleForDay build() {

        if (day == null)
            throw new IllegalStateException("Day must be set");

        return new ScheduleForDay(day, shifts.values());
    }
}
