package shift.scheduler.backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EntityViews.Public.class)
    public ResponseEntity<Collection<Company>> getAll() {

        Collection<Company> companies = companyService.findAll();

        if (companies == null || companies.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(companies);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EntityViews.Associate.class)
    public ResponseEntity<Company> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        User user = authenticationService.getUserFromHeader(authHeader);

        if (user == null)
            return ResponseEntity.badRequest().body(null);

        Company company = user.getCompany();

        if (company == null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(company);
    }

    @PostMapping(value = "/hours", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setHoursOfOperation(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                      @RequestBody Collection<HoursOfOperation> timePeriods) {

        Manager manager = (Manager) authenticationService.getUserFromHeader(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().body("Manager account does not exist");

        manager.getCompany().setHoursOfOperation(timePeriods);
        Company company = companyService.save(manager.getCompany());

        if (company == null)
            return ResponseEntity.badRequest().body("Failed to update hours of operation");
        else
            return ResponseEntity.ok("Successfully updated hours of operation");
    }


    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyDashboardData> getDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        Manager manager = (Manager) authenticationService.getUserFromHeader(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().build();

        Company company = manager.getCompany();

        LocalDate date = LocalDate.now();
        int today = date.getDayOfWeek().getValue();

        // TODO: Optimize schedule retrieval
        ScheduleForWeek schedule = scheduleService.findByCompanyAndDate(company, date);

        if (schedule == null)
            return ResponseEntity.noContent().build();

        CompanyDashboardData.DailyScheduleSummary nextDay = null;

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
                    if (shift.getStartHour() < start)
                        start = shift.getStartHour();

                    if (shift.getEndHour() > end)
                        end = shift.getEndHour();
                }

                nextDay = new CompanyDashboardData.DailyScheduleSummary(scheduleDate, start, end);
            }
        }

        int numEmployees = employeeHours.size();
        int totalHours = employeeHours.values().stream().reduce(0, (subtotal, hours) -> subtotal + hours, Integer::sum);

        return ResponseEntity.ok(new CompanyDashboardData(nextDay, numEmployees,  totalHours));
    }
}
