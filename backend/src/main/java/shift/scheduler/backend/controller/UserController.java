package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.repository.ManagerRepository;

@RestController
@RequestMapping("user")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final ManagerRepository managerRepository;

    private final EmployeeRepository employeeRepository;

    public UserController(PasswordEncoder passwordEncoder, ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping(value = "/manager/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerManager(@RequestBody Manager manager) {

        if (managerRepository.existsById(manager.getUsername()))
            return ResponseEntity.badRequest().body("Username taken");

        if (!User.validatePassword(manager.getPassword()))
            return ResponseEntity.badRequest().body("Invalid password");

        try {
            manager.setPasswordHash(passwordEncoder.encode(manager.getPassword()));
            managerRepository.save(manager);
            return ResponseEntity.ok("Account created");
        } catch (ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body("Invalid user details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping(value = "/employee/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {

        if (employeeRepository.existsById(employee.getUsername()))
            return ResponseEntity.badRequest().body("Username taken");

        if (!User.validatePassword(employee.getPassword()))
            return ResponseEntity.badRequest().body("Invalid password");

        try {
            employee.setPasswordHash(passwordEncoder.encode(employee.getPassword()));
            employeeRepository.save(employee);
            return ResponseEntity.ok("Account created");
        } catch (ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body("Invalid user details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String passwordHash = null;

        if (employeeRepository.existsById(username)) {
            Employee employee = employeeRepository.findById(username).get();
            passwordHash = employee.getPasswordHash();
        } else if (managerRepository.existsById(username)) {
            Manager manager = managerRepository.findById(username).get();
            passwordHash = manager.getPasswordHash();
        } else {
            ResponseEntity.badRequest().body("User not found");
        }

        // TODO: Complete implementation
        if (passwordEncoder.matches(password, passwordHash))
            return ResponseEntity.ok("Successfully logged in");
        else
            return ResponseEntity.badRequest().body("Invalid password");
    }
}
