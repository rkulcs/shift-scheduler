package shift.scheduler.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.dto.EmployeeSettingsDTO;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.dto.EmployeeDashboardDataDTO;
import shift.scheduler.backend.service.DashboardService;
import shift.scheduler.backend.service.UserService;


@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        var employee = (Employee) userService.findByAuthHeaderValue(authHeader);
        return ResponseEntity.ok(employee);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSettings(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                 @Valid @RequestBody EmployeeSettingsDTO settings) {

        var employee = (Employee) userService.findByAuthHeaderValue(authHeader);
        var isUpdated = userService.updateEmployeeSettings(employee, settings);

        if (isUpdated)
            return ResponseEntity.ok("Employee details updated");
        else
            return ResponseEntity.internalServerError().body("Failed to save employee details");
    }

    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDashboardDataDTO> getDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        var employee = (Employee) userService.findByAuthHeaderValue(authHeader);
        var data = dashboardService.getEmployeeDashboardData(employee);

        if (data == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(data);
    }
}
