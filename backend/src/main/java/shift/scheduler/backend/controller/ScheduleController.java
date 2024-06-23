package shift.scheduler.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.ScheduleForWeek;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.util.DateTimeUtil;

import java.time.LocalDate;

@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping(value = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleForWeek> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable String date) {

        User user = authenticationService.getUserFromHeader(authHeader);

        if (user.getCompany() == null)
            return ResponseEntity.badRequest().body(null);

        LocalDate parsedDate = DateTimeUtil.parseLocalDate(date);

        return ResponseEntity.ok(scheduleService.findByCompanyAndDate(user.getCompany(), parsedDate));
    }
}
