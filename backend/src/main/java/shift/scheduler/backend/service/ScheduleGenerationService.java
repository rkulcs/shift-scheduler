package shift.scheduler.backend.service;

import com.google.common.collect.Lists;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.combinations;

@Service
public class ScheduleGenerationService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private KieContainer kieContainer;

    public Collection<ScheduleForWeek> generateSchedulesForWeek(ScheduleGenerationRequest request, Company company) {

        if (company == null)
            return null;

        short numEmployeesPerHour = request.getNumEmployeesPerHour();
        var employees = company.getEmployees();

        if (employees.size() < numEmployeesPerHour)
            return null;

        Collection<Collection<ScheduleForDay>> candidateDailySchedules = new ArrayList<>();

        // Generate potential schedules for each day of the week
        for (var period : company.getHoursOfOperation()) {
            candidateDailySchedules.add(
                    generateCandidateSchedulesForDay(
                            period, numEmployeesPerHour,
                            employees.stream().filter(e -> e.isAvailableOn(period.getDay())).toList()
                    )
            );
        }

        // TODO: Find valid combinations of candidate daily schedules to create weekly schedules

        return new ArrayList<>();
    }

    private Collection<ScheduleForDay> generateCandidateSchedulesForDay(HoursOfOperation period,
                                                                        short numEmployeesPerHour,
                                                                        Collection<Employee> employees) {

        Collection<ScheduleForDay> candidateSchedules = new ArrayList<>();

        // Get the 4-hour blocks that make up the day's hours of operation
        List<TimePeriod> blocks = (List<TimePeriod>) period.getTimeBlocks();

        // Determine all possible combinations of employees for each block
        List<List<Set<Employee>>> employeeCombinations = new ArrayList<>();

        for (var block : blocks) {
            Set<Employee> availableEmployees = employees
                    .stream()
                    .filter(e -> e.isAvailableDuring(period.getDay(), block)).collect(Collectors.toSet());

            /* If there are not enough employees available, then it is not possible
               to generate a valid schedule for the day */
            if (availableEmployees.size() < numEmployeesPerHour)
                return null;

            employeeCombinations.add(combinations(availableEmployees, numEmployeesPerHour).stream().toList());
        }

        var products = Lists.cartesianProduct(employeeCombinations);

        for (var product : products) {
            ScheduleForDay schedule = createDailyScheduleIfValid(blocks, product);

            if (schedule != null)
                candidateSchedules.add(schedule);
        }

        return candidateSchedules;
    }

    private ScheduleForDay createDailyScheduleIfValid(List<TimePeriod> blocks, List<Set<Employee>> schedule) {

        int numBlocks = schedule.size();
        Map<Employee, Boolean[]> worksDuringBlock = new HashMap<>();

        for (int i = 0; i < numBlocks; i++) {
            for (Employee employee : schedule.get(i)) {
                if (!worksDuringBlock.containsKey(employee)) {
                    Boolean[] values = new Boolean[numBlocks];
                    Arrays.fill(values, false);
                    worksDuringBlock.put(employee, values);
                    worksDuringBlock.get(employee)[i] = true;
                }
            }
        }

        Collection<Shift> shifts = new ArrayList<>();

        for (Employee employee : worksDuringBlock.keySet()) {
            Boolean[] values = worksDuringBlock.get(employee);

            if (hasGap(values))
                return null;

            int first = -1;
            int last = -1;

            for (int i = 0; i < values.length; i++) {
                if (values[i] && first == -1)
                    first = last = i;
                else if (values[i])
                    last = i;
            }

            Shift shift = new Shift(blocks.get(first).getStartHour(), blocks.get(last).getEndHour(), employee);

            if (shift.getLength() < employee.getMinHoursPerDay() || shift.getLength() > employee.getMaxHoursPerDay())
                return null;

            shifts.add(shift);
        }

        ScheduleForDay scheduleForDay = new ScheduleForDay();
        scheduleForDay.setShifts(shifts);

        return scheduleForDay;
    }

    private boolean hasGap(Boolean[] values) {

        boolean startFound = false;
        boolean endFound = false;

        for (var value : values) {
            if (value && !endFound)
                startFound = true;
            else if (value && endFound)
                return true;
            else if (!value && startFound)
                endFound = true;
        }

        return false;
    }
}
