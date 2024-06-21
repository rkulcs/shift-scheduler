package shift.scheduler.backend.service;

import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.util.algorithm.DailyScheduleGenerator;
import shift.scheduler.backend.util.algorithm.WeeklyScheduleGenerator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ScheduleGenerationService {

    /* The difference between the integer representations of the Day enum and the
       Calendar type's day enums */
    private static final int DAY_TO_CALENDAR_DAY_SHIFT = 2;

    private class DailyScheduleWorker implements Runnable {
        private HoursOfOperation period;
        private short numEmployeesPerHour;
        private List<Employee> employees;
        private List<List<ScheduleForDay>> candidateDailySchedules;

        DailyScheduleWorker(HoursOfOperation period, short numEmployeesPerHour, List<Employee> employees, List<List<ScheduleForDay>> candidateDailySchedules) {
            this.period = period;
            this.numEmployeesPerHour = numEmployeesPerHour;
            this.employees = employees;
            this.candidateDailySchedules = candidateDailySchedules;
        }

        @Override
        public void run() {
            candidateDailySchedules.add(generateCandidateSchedulesForDay(period, numEmployeesPerHour, employees));
        }
    }

    public Collection<ScheduleForWeek> generateSchedulesForWeek(ScheduleGenerationRequest request, Company company) {

        if (company == null)
            return null;

        short numEmployeesPerHour = request.getNumEmployeesPerHour();
        var employees = company.getEmployees();

        if (employees.size() < numEmployeesPerHour)
            return null;

        List<List<ScheduleForDay>> candidateDailySchedules = new ArrayList<>();

        Collection<Thread> threads = new ArrayList<>();

        // Generate potential schedules for each day of the week
        for (var period : company.getHoursOfOperation()) {
            DailyScheduleWorker g = new DailyScheduleWorker(
                    period, numEmployeesPerHour,
                    employees.stream().filter(e -> e.isAvailableOn(period.getDay())).toList(),
                    candidateDailySchedules
            );
            Thread t = new Thread(g);
            threads.add(t);
            t.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        WeeklyScheduleGenerator weeklyScheduleGenerator = new WeeklyScheduleGenerator();
        Collection<ScheduleForWeek> schedules = weeklyScheduleGenerator.generateSchedules(candidateDailySchedules);

        Map<Day, LocalDate> dates = generateDateMap(request.getDate());

        for (ScheduleForWeek schedule : schedules) {
            schedule.setFirstDay(dates.get(Day.MON));
            schedule.setCompany(company);

            for (ScheduleForDay dailySchedule : schedule.getDailySchedules())
                dailySchedule.setDate(dates.get(dailySchedule.getDay()));
        }

        return schedules;
    }

    private List<ScheduleForDay> generateCandidateSchedulesForDay(HoursOfOperation period,
                                                                 short numEmployeesPerHour,
                                                                 Collection<Employee> employees) {

        List<List<Shift>> potentialShifts = new ArrayList<>();
        employees.forEach(e -> potentialShifts.add((List<Shift>) e.generatePotentialShifts(period)));

        DailyScheduleGenerator generator = new DailyScheduleGenerator(period, numEmployeesPerHour);
        List<ScheduleForDay> generatedSchedules = (List<ScheduleForDay>) generator.generateSchedules(potentialShifts);

        generatedSchedules.forEach(schedule -> schedule.setDay(period.getDay()));

        return generatedSchedules;
    }

    /**
     * Creates a map in which a day can be used as the key to get a corresponding date.
     *
     * @param date The date of any day during the week
     */
    private Map<Day, LocalDate> generateDateMap(LocalDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(date.getYear(), date.getMonth().getValue()-1, date.getDayOfMonth());
        Map<Day, LocalDate> dates = new HashMap<>();

        for (Day day : Day.values()) {
            if (day.equals(Day.SUN))
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            else
                calendar.set(Calendar.DAY_OF_WEEK, day.ordinal()+DAY_TO_CALENDAR_DAY_SHIFT);

            dates.put(day, LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault()));
        }

        return dates;
    }
}
