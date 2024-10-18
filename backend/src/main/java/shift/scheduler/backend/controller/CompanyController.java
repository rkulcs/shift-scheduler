package shift.scheduler.backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.dto.CompanyDashboardDataDTO;
import shift.scheduler.backend.dto.TimePeriodDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.DashboardService;
import shift.scheduler.backend.service.UserService;

import java.util.*;

@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DashboardService dashboardService;

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

        User user = userService.findByAuthHeaderValue(authHeader);

        Company company = user.getCompany();

        if (company == null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(company);
    }

    @PostMapping(value = "/hours", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setHours(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                @RequestBody Collection<TimePeriodDTO> timePeriods) {

        Manager manager = (Manager) userService.findByAuthHeaderValue(authHeader);
        var updateIsSuccessful = companyService.updateHoursOfOperation(manager.getCompany(), timePeriods);

        if (updateIsSuccessful)
            return ResponseEntity.ok("Successfully updated hours of operation");
        else
            return ResponseEntity.badRequest().body("Failed to update hours of operation");
    }


    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyDashboardDataDTO> getDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        Manager manager = (Manager) userService.findByAuthHeaderValue(authHeader);
        var data = dashboardService.getCompanyDashboardData(manager);

        if (data == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(data);
    }
}
