package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.repository.ManagerRepository;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping(value = "/manager/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerManager(@RequestBody Manager manager) {

        if (managerRepository.existsById(manager.getUsername()))
            return ResponseEntity.badRequest().body("Username taken");

        try {
            managerRepository.save(manager);
            return ResponseEntity.ok("Account created");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Invalid user details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping(value = "/employee/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {

        if (employeeRepository.existsById(employee.getUsername()))
            return ResponseEntity.badRequest().body("Username taken");

        try {
            employeeRepository.save(employee);
            return ResponseEntity.ok("Account created");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Invalid user details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
}
