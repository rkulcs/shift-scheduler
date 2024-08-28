package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.*;

import static shift.scheduler.backend.service.AuthenticationService.AuthenticationResult;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResult> register(@RequestBody RegistrationRequestDTO request) {

        AuthenticationResult result = authenticationService.register(request);

        if (result.getError() != null)
            return ResponseEntity.badRequest().body(result);
        else
            return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResult> login(@RequestBody LoginRequestDTO request) {

        AuthenticationResult result = authenticationService.login(request);

        if (result.getError() != null)
            return ResponseEntity.badRequest().body(result);
        else
            return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String[] authComponents = authHeader.split(" ");

        if (authComponents.length == 0)
            return ResponseEntity.badRequest().body("The request was not sent as a logged-in user.");

        String token = authComponents[authComponents.length-1];

        if (authenticationService.logout(token))
            return ResponseEntity.ok("Successfully logged out.");
        else
            return ResponseEntity.internalServerError().body("Failed to log out.");
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity delete(@PathVariable String username) {

        boolean isDeleted = false;

        if (managerService.existsByUsername(username))
            isDeleted = managerService.deleteByUsername(username);
        else if (employeeService.existsByUsername(username))
            isDeleted = employeeService.deleteByUsername(username);
        else
            return ResponseEntity.internalServerError().body("User does not exist.");

        if (isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().body("Failed to delete user.");
    }
}
