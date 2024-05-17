package shift.scheduler.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shift.scheduler.backend.entity.Manager;

@RestController
public class UserController {

    @PostMapping("/manager/register")
    public ResponseEntity<String> registerManager(Manager manager) {

        return ResponseEntity.ok("Account created");
    }
}
