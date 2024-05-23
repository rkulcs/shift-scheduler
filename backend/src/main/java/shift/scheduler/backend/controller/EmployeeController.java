package shift.scheduler.backend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Availability;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.repository.AvailabilityRepository;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.JwtService;

import java.util.Collection;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @PostMapping(value = "/availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAvailabilities(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                       @RequestBody Collection<Availability> availabilities) {

        Employee employee = getEmployee(authHeader);

        if (employee == null)
            return ResponseEntity.badRequest().body("Employee does not exist");

        availabilities.forEach(availability -> availability.setEmployee(employee));

        try {
            availabilityRepository.saveAll(availabilities);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }

        return ResponseEntity.ok("Availabilities added");
    }

    private Employee getEmployee(String authHeader) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String username = jwtService.extractUsername(token);
        return (Employee) employeeService.findByUsername(username);
    }
}
