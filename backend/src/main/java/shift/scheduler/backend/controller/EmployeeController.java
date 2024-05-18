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

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // TODO: Perform authorization
    @PostMapping(value = "/{username}/availability/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newAvailability(@RequestBody Availability availability,
                                                  @PathVariable String username) {

        Employee employee = employeeRepository.findById(username).orElse(null);

        if (employee == null)
            return ResponseEntity.badRequest().body("Employee does not exist");

        availability.setEmployee(employee);

        try {
            availabilityRepository.save(availability);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Invalid details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }

        return ResponseEntity.ok("Availability added");
    }
}
