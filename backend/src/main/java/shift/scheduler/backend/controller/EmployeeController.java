package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.EmployeeDashboardData;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.repository.AvailabilityRepository;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.JwtService;
import shift.scheduler.backend.service.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        Employee employee = (Employee) authenticationService.getUserFromHeader(authHeader);

        if (employee == null)
            return ResponseEntity.badRequest().body(null);
        else
            return ResponseEntity.ok(employee);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> post(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                       @RequestBody Employee newEmployeeDetails) {

        Employee employee = (Employee) authenticationService.getUserFromHeader(authHeader);

        employee.setAvailabilities(newEmployeeDetails.getAvailabilities());
        employee.setMinHoursPerDay(newEmployeeDetails.getMinHoursPerDay());
        employee.setMaxHoursPerDay(newEmployeeDetails.getMaxHoursPerDay());
        employee.setMinHoursPerWeek(newEmployeeDetails.getMinHoursPerWeek());
        employee.setMaxHoursPerWeek(newEmployeeDetails.getMaxHoursPerWeek());

        try {
            employeeService.save(employee);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }

        return ResponseEntity.ok("Employee details updated");
    }


    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDashboardData> getDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        Employee employee = (Employee) authenticationService.getUserFromHeader(authHeader);
        LocalDate date = LocalDate.now();
        int today = date.getDayOfWeek().getValue();

        // TODO: Optimize shift retrieval
        ScheduleForWeek schedule = scheduleService.findByCompanyAndDate(employee.getCompany(), date);

        if (schedule == null)
            return ResponseEntity.noContent().build();

        List<Shift> shifts = new ArrayList<>();
        EmployeeDashboardData.DetailedShiftData nextShift = null;

        for (ScheduleForDay dailySchedule : schedule.getDailySchedules()) {
            Shift shift = dailySchedule.getShifts().stream().filter(s -> s.getEmployee().equals(employee)).findFirst().orElse(null);

            if (shift == null)
                continue;

            shifts.add(shift);

            int day = (dailySchedule.getDay().ordinal() + 1) % 7;

            if (nextShift == null && day >= today) {
                LocalDate shiftDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(day)));
                nextShift = new EmployeeDashboardData.DetailedShiftData(shift, shiftDate);
            }
        }

        int numHours = shifts.stream().reduce(0, (subtotal, shift) -> subtotal + shift.getLength(), Integer::sum);

        EmployeeDashboardData data = new EmployeeDashboardData(nextShift, shifts.size(), numHours);

        return ResponseEntity.ok(data);
    }
}
