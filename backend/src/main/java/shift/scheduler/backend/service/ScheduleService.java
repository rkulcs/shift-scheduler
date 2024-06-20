package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.ScheduleForWeek;
import shift.scheduler.backend.repository.ScheduleForWeekRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

@Service
public class ScheduleService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleForWeekRepository scheduleForWeekRepository;

    public ScheduleForWeek save(ScheduleForWeek schedule) throws Exception {

        // TODO: Check if previous version of schedule needs to be updated
        schedule.getFirstDay()

        schedule.getDailySchedules().forEach(dailySchedule -> {
            dailySchedule.getShifts().forEach(shift -> shift.setEmployee((Employee) employeeService.findByUsername(shift.getEmployee().getUsername())));
        });

        return scheduleForWeekRepository.save(schedule);
    }
}
