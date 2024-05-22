package shift.scheduler.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shift.scheduler.backend.model.TimePeriod;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;

import java.util.Collection;

@RestController
@RequestMapping("manager")
public class ManagerController {

    @PostMapping(value = "/hours-of-operation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateSchedules(@RequestBody Collection<TimePeriod> timePeriods) {
        return null;
    }

    @PostMapping(value = "/generate-schedules", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateSchedules(@RequestBody ScheduleGenerationRequest request) {
        return null;
    }
}