package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.repository.ScheduleForWeekRepository;
import shift.scheduler.backend.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ScheduleForWeekRepository scheduleForWeekRepository;

    public Optional<ScheduleForWeek> findByCompanyAndDate(Company company, LocalDate date) {

        LocalDate firstDayOfWeek = DateTimeUtil.getFirstDayOfWeek(date);

        return scheduleForWeekRepository.findByCompanyAndFirstDay(company, firstDayOfWeek);
    }

    public Optional<ScheduleForWeek> save(ScheduleForWeek schedule) {

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
            dailySchedule.getShifts().forEach(shift -> shift.setEmployee((Employee) userService.findByUsername(shift.getEmployee().getUsername()).get()));
        });

        try {
            return Optional.of(scheduleForWeekRepository.save(schedule));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
