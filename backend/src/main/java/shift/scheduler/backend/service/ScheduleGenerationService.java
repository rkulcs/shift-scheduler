package shift.scheduler.backend.service;

import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.model.schedule.Schedule;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.dto.ScheduleGenerationRequestDTO;
import shift.scheduler.backend.util.DateTimeUtil;
import shift.scheduler.backend.util.algorithm.DailyScheduleGenerator;
import shift.scheduler.backend.util.algorithm.WeeklyScheduleGenerator;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleGenerationService {

    /**
     * Generates a schedule for a given day's hours of operation.
     */
    private static class DailyScheduleWorker implements Runnable {
        private final TimePeriod period;
        private final short numEmployeesPerHour;
        private final List<Employee> employees;
        private final List<List<Schedule>> candidateDailySchedules;

        DailyScheduleWorker(TimePeriod period, short numEmployeesPerHour, List<Employee> employees, List<List<Schedule>> candidateDailySchedules) {
            this.period = period;
            this.numEmployeesPerHour = numEmployeesPerHour;
            this.employees = employees;
            this.candidateDailySchedules = candidateDailySchedules;
        }

        @Override
        public void run() {
            candidateDailySchedules.add(generateCandidateSchedulesForDay());
        }

        private List<Schedule> generateCandidateSchedulesForDay() {

        /* For each employee, generate all possible shifts that they could work on this day based
           on their availability */
            List<List<Shift>> potentialShifts = new ArrayList<>();
            employees.forEach(e -> potentialShifts.add((List<Shift>) e.generatePotentialShifts(period)));

            // Generate possible schedules for the day using a genetic algorithm
            DailyScheduleGenerator generator = new DailyScheduleGenerator(period, numEmployeesPerHour);
            Collection<Schedule> generatedSchedules = generator.generateSchedules(potentialShifts);

            generatedSchedules.forEach(schedule -> ((ScheduleForDay) schedule).setDay(period.getDay()));

            return (List<Schedule>) generatedSchedules;
        }
    }

    public Collection<ScheduleForWeek> generateSchedulesForWeek(ScheduleGenerationRequestDTO request, Company company) {

        if (company == null)
            return null;

        short numEmployeesPerHour = request.numEmployeesPerHour();
        var employees = company.getEmployees();

        if (employees.size() < numEmployeesPerHour)
            return null;

        List<List<Schedule>> candidateDailySchedules = new ArrayList<>();

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
                // TODO: Log exception
                System.out.println(e);
            }
        }

        // Generate possible weekly schedules using the generated daily schedules
        WeeklyScheduleGenerator weeklyScheduleGenerator = new WeeklyScheduleGenerator();
        Collection<Schedule> schedules = weeklyScheduleGenerator.generateSchedules((List<List<ScheduleForDay>>) (Object) candidateDailySchedules);

        LocalDate firstDay = DateTimeUtil.getFirstDayOfWeek(request.date());

        Collection<ScheduleForWeek> weeklySchedules = schedules.stream().map(schedule -> (ScheduleForWeek) schedule).toList();

        weeklySchedules.forEach(schedule -> {
            schedule.setFirstDay(firstDay);
            schedule.setCompany(company);
        });

        return weeklySchedules;
    }
}
