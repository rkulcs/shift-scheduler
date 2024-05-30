package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.util.DailyScheduleGenerator;

import java.util.*;

@Service
public class ScheduleGenerationService {

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

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WeeklyScheduleGenerationService weeklyScheduleGenerationService;

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

        Collection<ScheduleForWeek> schedules = weeklyScheduleGenerationService.generateSchedules(candidateDailySchedules);

        return schedules;
    }

    private List<ScheduleForDay> generateCandidateSchedulesForDay(HoursOfOperation period,
                                                                 short numEmployeesPerHour,
                                                                 Collection<Employee> employees) {

        List<List<Shift>> potentialShifts = new ArrayList<>();
        employees.forEach(e -> potentialShifts.add((List<Shift>) e.generatePotentialShifts(period)));

        // Get the 4-hour blocks that make up the day's hours of operation
        List<TimePeriod> blocks = (List<TimePeriod>) period.getTimeBlocks();

        DailyScheduleGenerator generator = new DailyScheduleGenerator(period, numEmployeesPerHour);
        return (List<ScheduleForDay>) generator.generateSchedules(potentialShifts);
    }
}
