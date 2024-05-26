package shift.scheduler.backend.service;

import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleGenerationService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private KieContainer kieContainer;
}
