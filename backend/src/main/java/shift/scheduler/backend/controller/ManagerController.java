package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.HoursOfOperation;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.TimePeriod;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.JwtService;
import shift.scheduler.backend.service.ManagerService;

import java.util.Collection;

@RestController
@RequestMapping("manager")
public class ManagerController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "/hours-of-operation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setHoursOfOperation(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                      @RequestBody Collection<HoursOfOperation> timePeriods) {

        Manager manager = getManager(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().body("Manager account does not exist");

        manager.getCompany().setHoursOfOperation(timePeriods);
        Company company = companyService.save(manager.getCompany());

        if (company == null)
            return ResponseEntity.badRequest().body("No company is associated with this manager");
        else
            return ResponseEntity.ok("Successfully updated hours of operation");
    }

    @PostMapping(value = "/generate-schedules", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateSchedules(@RequestBody ScheduleGenerationRequest request) {
        return null;
    }

    private Manager getManager(String authHeader) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String username = jwtService.extractUsername(token);
        return (Manager) managerService.findByUsername(username);
    }
}