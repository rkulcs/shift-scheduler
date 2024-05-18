package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Availability;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.repository.AvailabilityRepository;
import shift.scheduler.backend.repository.EmployeeRepository;

import java.util.Collection;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // TODO: Perform authorization
    @PostMapping(value = "/{username}/availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newAvailabilities(@RequestBody Collection<Availability> availabilities,
                                                  @PathVariable String username) {

        Employee employee = employeeRepository.findById(username).orElse(null);

        if (employee == null)
            return ResponseEntity.badRequest().body("Employee does not exist");

        availabilities.forEach(availability -> availability.setEmployee(employee));

        try {
            availabilityRepository.saveAll(availabilities);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Invalid details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }

        return ResponseEntity.ok("Availabilities added");
    }
}
