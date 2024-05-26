package shift.scheduler.backend.service;

import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ScheduleGenerationService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private KieContainer kieContainer;

    public Collection<ScheduleForWeek> generateSchedulesForWeek(ScheduleGenerationRequest request, Company company) {

        if (company == null)
            return null;

        int numEmployeesPerHour = request.getNumEmployeesPerHour();
        var employees = company.getEmployees();

        if (employees.size() < numEmployeesPerHour)
            return null;

        Collection<Collection<ScheduleForDay>> candidateDailySchedules = new ArrayList<>();

        // Generate potential schedules for each day of the week
        for (var period : company.getHoursOfOperation()) {
            candidateDailySchedules.add(generateCandidateSchedulesForDay(period, employees));
        }

        // TODO: Find valid combinations of candidate daily schedules to create weekly schedules

        return new ArrayList<>();
    }

    private Collection<ScheduleForDay> generateCandidateSchedulesForDay(HoursOfOperation period,
                                                                        Collection<Employee> employees) {

        Collection<ScheduleForDay> candidateSchedules = new ArrayList<>();

        // TODO: Generate possible shifts on the given day for all employees

        // TODO: Generate possible daily schedules based on the potential shifts that were generated

        return candidateSchedules;
    }
}
