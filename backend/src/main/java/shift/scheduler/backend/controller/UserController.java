package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;

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
    public ResponseEntity<String> login(@RequestBody Account account) {

        boolean isValid = false;

        if (employeeService.existsByUsername(account.getUsername())) {
            User user = employeeService.findByUsername(account.getUsername());
            isValid = accountService.validatePassword(account, user);
        } else if (managerService.existsByUsername(account.getUsername())) {
            User user = managerService.findByUsername(account.getUsername());
            isValid = accountService.validatePassword(account, user);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }

        String token = jwtService.generateToken(account);

        // TODO: Complete implementation
        if (isValid)
            return ResponseEntity.ok(token);
        else
            return ResponseEntity.badRequest().body("Invalid password");
    }
}
