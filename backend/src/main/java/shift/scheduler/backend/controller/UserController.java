package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.ManagerService;
import shift.scheduler.backend.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    public ResponseEntity<String> register(User user, UserService service) {

        try {
            service.save(user);
            return ResponseEntity.ok("Account created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/manager/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerManager(@RequestBody Manager manager) {
        return register(manager, managerService);
    }

    @PostMapping(value = "/employee/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {
        return register(employee, employeeService);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody User user) {

        boolean isValid = false;

        if (employeeService.existsByUsername(user.getUsername())) {
            isValid = employeeService.validatePassword(user);
        } else if (managerService.existsByUsername(user.getUsername())) {
            isValid = employeeService.validatePassword(user);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }

        // TODO: Complete implementation
        if (isValid)
            return ResponseEntity.ok("Successfully logged in");
        else
            return ResponseEntity.badRequest().body("Invalid password");
    }
}
