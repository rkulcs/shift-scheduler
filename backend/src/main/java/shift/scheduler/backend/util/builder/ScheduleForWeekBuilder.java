package shift.scheduler.backend.util.builder;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ScheduleForWeekBuilder implements Builder<ScheduleForWeek> {

    private Company company;

    private LocalDate firstDay;

    private final Map<Day, ScheduleForDayBuilder> scheduleForDayBuilders = new HashMap<>();

    public ScheduleForDayBuilder getScheduleForDayBuilder(Day day) {

        var builder = scheduleForDayBuilders.get(day);

        if (builder == null) {
            builder = new ScheduleForDayBuilder();
            builder.setDay(day);

            scheduleForDayBuilders.put(day, builder);
        }

        return builder;
    }

    public ScheduleForWeekBuilder setCompany(Company company) {
        this.company = company;
        return this;
    }

    public ScheduleForWeekBuilder setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
        return this;
    }

    public ScheduleForWeekBuilder addShift(Employee employee, Day day, int start, int end) {
        getScheduleForDayBuilder(day).addShift(employee, start, end);
        return this;
    }

    public ScheduleForWeekBuilder addShift(Day day, Shift shift) {
        getScheduleForDayBuilder(day).addShift(shift);
        return this;
    }

    @Override
    public ScheduleForWeek build() {

        if (company == null)
            throw new IllegalStateException("Company must be set");
        if (firstDay == null)
            throw new IllegalStateException("First day must be set");

        var schedule = new ScheduleForWeek(scheduleForDayBuilders.values().stream().map(builder -> builder.build()).toList());
        schedule.setCompany(company);
        schedule.setFirstDay(firstDay);

        return schedule;
    }
}
