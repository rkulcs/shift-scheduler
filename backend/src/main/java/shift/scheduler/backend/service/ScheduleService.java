package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.repository.ScheduleForWeekRepository;
import shift.scheduler.backend.util.DateTimeUtil;

import java.time.LocalDate;

@Service
public class ScheduleService {

    @Autowired
    private UserService<Employee> employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ScheduleForWeekRepository scheduleForWeekRepository;

    public ScheduleForWeek findByCompanyAndDate(Company company, LocalDate date) {

        if (company == null || date == null)
            return null;

        LocalDate firstDayOfWeek = DateTimeUtil.getFirstDayOfWeek(date);

        return scheduleForWeekRepository.findByCompanyAndFirstDay(company, firstDayOfWeek).orElse(null);
    }

    public ScheduleForWeek save(ScheduleForWeek schedule) throws Exception {

        schedule.setCompany(companyService.findById(schedule.getCompany().getId()));

        LocalDate firstDayOfWeek = DateTimeUtil.getFirstDayOfWeek(schedule.getFirstDay());
        schedule.setFirstDay(firstDayOfWeek);

        /* Check if a weekly schedule already exists for the given date, and if so, update that instead of
           creating a new weekly schedule in the database */
        ScheduleForWeek previousVersion = scheduleForWeekRepository
                .findByCompanyAndFirstDay(schedule.getCompany(), schedule.getFirstDay()).orElse(null);

        if (previousVersion != null) {
            previousVersion.setDailySchedules(schedule.getDailySchedules());
            schedule = previousVersion;
        }

        schedule.getDailySchedules().forEach(dailySchedule -> {
            dailySchedule.getShifts().forEach(shift -> shift.setEmployee((Employee) employeeService.findByUsername(shift.getEmployee().getUsername())));
        });

        return scheduleForWeekRepository.save(schedule);
    }
}
