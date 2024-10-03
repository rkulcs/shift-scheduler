package shift.scheduler.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.service.AuthenticationService;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequestDTO request) {

        var result = authenticationService.register(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO request) {

        var result = authenticationService.login(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        authenticationService.logout(authHeader);
        return ResponseEntity.ok().build();
    }
}
