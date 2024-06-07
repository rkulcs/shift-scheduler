package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.service.*;

import java.util.Collection;

@RestController
@RequestMapping("manager")
public class ManagerController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ScheduleGenerationService scheduleGenerationService;

    @PostMapping(value = "/hours-of-operation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setHoursOfOperation(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                      @RequestBody Collection<HoursOfOperation> timePeriods) {

        Manager manager = (Manager) authenticationService.getUserFromHeader(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().body("Manager account does not exist");

        manager.getCompany().setHoursOfOperation(timePeriods);
        Company company = companyService.save(manager.getCompany());

        if (company == null)
            return ResponseEntity.badRequest().body("No company is associated with this manager");
        else
            return ResponseEntity.ok("Successfully updated hours of operation");
    }

    @PostMapping(value = "/generate-schedules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ScheduleForWeek>> generateSchedules(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                         @RequestBody ScheduleGenerationRequest request) {

        Manager manager = (Manager) authenticationService.getUserFromHeader(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().body(null);

        Company company = manager.getCompany();

        Collection<ScheduleForWeek> schedules = scheduleGenerationService.generateSchedulesForWeek(request, company);

        if (schedules.isEmpty())
            return ResponseEntity.unprocessableEntity().body(schedules);
        else
            return ResponseEntity.ok(schedules);
    }
}