package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.service.*;

import static shift.scheduler.backend.service.AuthenticationService.AuthenticationResult;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

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
}
