package shift.scheduler.backend.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.ScheduleGenerationService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleGenerationService scheduleGenerationService;

    @GetMapping(value = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleForWeek> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable String date) {

        User user = authenticationService.getUserFromHeader(authHeader);

        if (user == null || user.getCompany() == null)
            return ResponseEntity.badRequest().body(null);

        LocalDate parsedDate = DateTimeUtil.parseLocalDate(date);

        if (parsedDate == null)
            return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok(scheduleService.findByCompanyAndDate(user.getCompany(), parsedDate));
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("MANAGER")
    public ResponseEntity<Collection<ScheduleForWeek>> generate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                @RequestBody ScheduleGenerationRequest request) {

        Manager manager = (Manager) authenticationService.getUserFromHeader(authHeader);

        if (manager == null)
            return ResponseEntity.badRequest().body(null);

        Company company = manager.getCompany();

        Collection<ScheduleForWeek> schedules = scheduleGenerationService.generateSchedulesForWeek(request, company);

        if (schedules.isEmpty())
            return ResponseEntity.unprocessableEntity().body(schedules);
        else
            return ResponseEntity.ok(schedules);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("MANAGER")
    public ResponseEntity<String> save(@RequestBody ScheduleForWeek schedule) {

        try {
            ScheduleForWeek savedSchedule = scheduleService.save(schedule);
            return ResponseEntity.ok("Schedule saved");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
