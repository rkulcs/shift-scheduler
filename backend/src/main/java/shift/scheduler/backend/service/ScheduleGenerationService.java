package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleGenerationService {

    @Autowired
    private EmployeeService employeeService;
}
