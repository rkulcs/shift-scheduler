package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.dto.CompanyDashboardDataDTO;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.dto.EmployeeDashboardDataDTO;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.schedule.ScheduleForDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private ScheduleService scheduleService;

    public EmployeeDashboardDataDTO getEmployeeDashboardData(Employee employee) {

        LocalDate date = LocalDate.now();
        int today = date.getDayOfWeek().getValue();

        // TODO: Optimize shift retrieval
        var scheduleQueryResult = scheduleService.findByCompanyAndDate(employee.getCompany(), date);

        if (scheduleQueryResult.isEmpty())
            return null;

        var schedule = scheduleQueryResult.get();

        List<Shift> shifts = new ArrayList<>();

        EmployeeDashboardDataDTO.DetailedShiftDataDTO nextShift = null;

        // Keep track of the number of hours that the employee will work this week
        int numHours = 0;

        for (ScheduleForDay dailySchedule : schedule.getDailySchedules()) {
            Shift shift = dailySchedule.getShifts().stream().filter(s -> s.getEmployee().equals(employee)).findFirst().orElse(null);

            if (shift == null)
                continue;

            shifts.add(shift);
            numHours += shift.getLength();

            int day = dailySchedule.getDay().toDayOfWeekValue();

            // Set the next details of the next shift that the employee will work
            if (nextShift == null && day >= today) {
                LocalDate shiftDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)));
                nextShift = new EmployeeDashboardDataDTO.DetailedShiftDataDTO(shift, shiftDate);
            }
        }

        return new EmployeeDashboardDataDTO(nextShift, shifts.size(), numHours);
    }

    public CompanyDashboardDataDTO getCompanyDashboardData(Manager manager) {

        Company company = manager.getCompany();

        LocalDate date = LocalDate.now();
        int today = date.getDayOfWeek().getValue();

        // TODO: Optimize schedule retrieval
        var scheduleQueryResult = scheduleService.findByCompanyAndDate(company, date);

        if (scheduleQueryResult.isEmpty())
            return null;

        var schedule = scheduleQueryResult.get();

        CompanyDashboardDataDTO.DailyScheduleSummary nextDay = null;

        // Keep track of all employees who will work this week
        Set<Employee> employees = new HashSet<>();

        // Keep track of the total number of employee hours
        int totalHours = 0;

        for (ScheduleForDay dailySchedule : schedule.getDailySchedules()) {
            for (Shift shift : dailySchedule.getShifts()) {
                employees.add(shift.getEmployee());
                totalHours += shift.getLength();
            }

            int day = dailySchedule.getDay().toDayOfWeekValue();

            // Set the details of the next day of operations
            if (nextDay == null && day >= today) {
                LocalDate scheduleDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)));

                int start = Integer.MAX_VALUE;
                int end = -Integer.MAX_VALUE;

                // Get the start and end hours of the day
                for (Shift shift : dailySchedule.getShifts()) {
                    if (shift.getStart() < start)
                        start = shift.getStart();

                    if (shift.getEnd() > end)
                        end = shift.getEnd();
                }

                nextDay = new CompanyDashboardDataDTO.DailyScheduleSummary(scheduleDate, start, end);
            }
        }

        int numEmployees = employees.size();

        return new CompanyDashboardDataDTO(nextDay, numEmployees, totalHours);
    }
}
