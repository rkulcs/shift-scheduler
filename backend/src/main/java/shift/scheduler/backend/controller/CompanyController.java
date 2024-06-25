package shift.scheduler.backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.HoursOfOperation;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.CompanyService;

import java.util.Collection;

@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

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
            return ResponseEntity.badRequest().body("No company is associated with this manager");
        else
            return ResponseEntity.ok("Successfully updated hours of operation");
    }
}
