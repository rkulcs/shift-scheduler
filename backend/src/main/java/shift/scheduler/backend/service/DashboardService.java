package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.dto.EmployeeDashboardDataDTO;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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
}
