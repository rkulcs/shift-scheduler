package shift.scheduler.backend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.dto.ScheduleGenerationRequestDTO;
import shift.scheduler.backend.service.ScheduleGenerationService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleGenerationService scheduleGenerationService;

    @GetMapping(value = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleForWeek> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable LocalDate date) {

        User user = userService.findByAuthHeaderValue(authHeader);
        return ResponseEntity.ok(scheduleService.findByCompanyAndDate(user.getCompany(), date));
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("MANAGER")
    public ResponseEntity<Collection<ScheduleForWeek>> generate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                @Valid @RequestBody ScheduleGenerationRequestDTO request) {

        Manager manager = (Manager) userService.findByAuthHeaderValue(authHeader);
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
            scheduleService.save(schedule);
            return ResponseEntity.ok("Schedule saved");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
