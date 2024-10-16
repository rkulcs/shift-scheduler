package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        for (ScheduleForDay dailySchedule : schedule.getDailySchedules()) {
            Shift shift = dailySchedule.getShifts().stream().filter(s -> s.getEmployee().equals(employee)).findFirst().orElse(null);

            if (shift == null)
                continue;

            shifts.add(shift);

            int day = (dailySchedule.getDay().ordinal() + 1) % 7;

            if (nextShift == null && day >= today) {
                LocalDate shiftDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)));
                nextShift = new EmployeeDashboardDataDTO.DetailedShiftDataDTO(shift, shiftDate);
            }
        }

        int numHours = shifts.stream().reduce(0, (subtotal, shift) -> subtotal + shift.getLength(), Integer::sum);

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

        Map<Employee, Integer> employeeHours = new HashMap<>();

        for (ScheduleForDay dailySchedule : schedule.getDailySchedules()) {
            dailySchedule.getShifts().forEach(shift -> {
                Employee employee = shift.getEmployee();

                int newHours = shift.getLength();

                if (employeeHours.containsKey(employee))
                    newHours += employeeHours.get(employee);

                employeeHours.put(employee, newHours);
            });

            int day = (dailySchedule.getDay().ordinal() + 1) % 7;

            if (nextDay == null && day >= today) {
                LocalDate scheduleDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)));

                int start = Integer.MAX_VALUE;
                int end = -Integer.MAX_VALUE;

                for (Shift shift : dailySchedule.getShifts()) {
                    if (shift.getStart() < start)
                        start = shift.getStart();

                    if (shift.getEnd() > end)
                        end = shift.getEnd();
                }

                nextDay = new CompanyDashboardDataDTO.DailyScheduleSummary(scheduleDate, start, end);
            }
        }

        int numEmployees = employeeHours.size();
        int totalHours = employeeHours.values().stream().reduce(0, (subtotal, hours) -> subtotal + hours, Integer::sum);

        return new CompanyDashboardDataDTO(nextDay, numEmployees,  totalHours);
    }
}
