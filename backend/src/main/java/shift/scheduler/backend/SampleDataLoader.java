package shift.scheduler.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.service.UserService;
import shift.scheduler.backend.util.builder.CompanyBuilder;
import shift.scheduler.backend.util.builder.EmployeeBuilder;
import shift.scheduler.backend.util.builder.ManagerBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeds the database that used in development builds with some sample data.
 */
@Component
@Profile("dev")
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService<Manager> managerService;

    @Autowired
    private UserService<Employee> employeeService;

    @Override
    public void run(String... args) throws Exception {
        Company company = createCompany();
        createSampleEmployees(company);
    }

    private Company createCompany() throws Exception {

        Company company = new CompanyBuilder()
                .setName("Company")
                .addHoursOfOperation(Day.MON, 12, 20)
                .addHoursOfOperation(Day.TUE, 12, 20)
                .addHoursOfOperation(Day.WED, 8, 20)
                .addHoursOfOperation(Day.THU, 8, 20)
                .addHoursOfOperation(Day.FRI, 8, 20)
                .addHoursOfOperation(Day.SAT, 8, 16)
                .addHoursOfOperation(Day.SUN, 8, 16)
                .build();

        Manager manager = new ManagerBuilder()
                .setUsername("sampleManager")
                .setPasswordEncoder(passwordEncoder)
                .setCompany(company)
                .build();

        managerService.save(manager);

        return company;
    }

    private void createSampleEmployees(Company company) throws Exception {

        List<Employee> employees = new ArrayList<>();

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee1")
                        .setName("Employee 1")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(8, 12)
                        .setHoursPerWeekRange(32, 40)
                        .addAvailability(Day.MON, 4, 20)
                        .addAvailability(Day.TUE, 4, 20)
                        .addAvailability(Day.WED, 4, 20)
                        .addAvailability(Day.THU, 4, 20)
                        .addAvailability(Day.FRI, 4, 20)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee2")
                        .setName("Employee 2")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 12)
                        .setHoursPerWeekRange(36, 48)
                        .addAvailability(Day.MON, 8, 24)
                        .addAvailability(Day.TUE, 8, 24)
                        .addAvailability(Day.WED, 8, 24)
                        .addAvailability(Day.THU, 8, 24)
                        .addAvailability(Day.FRI, 8, 24)
                        .addAvailability(Day.SAT, 8, 24)
                        .addAvailability(Day.SUN, 8, 24)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee3")
                        .setName("Employee 3")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 8)
                        .setHoursPerWeekRange(32, 40)
                        .addAvailability(Day.TUE, 0, 24)
                        .addAvailability(Day.WED, 0, 24)
                        .addAvailability(Day.THU, 0, 24)
                        .addAvailability(Day.FRI, 0, 24)
                        .addAvailability(Day.SAT, 0, 24)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee4")
                        .setName("Employee 4")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 12)
                        .setHoursPerWeekRange(36, 40)
                        .addAvailability(Day.MON, 8, 24)
                        .addAvailability(Day.TUE, 8, 24)
                        .addAvailability(Day.WED, 8, 24)
                        .addAvailability(Day.FRI, 8, 24)
                        .addAvailability(Day.SAT, 0, 24)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee5")
                        .setName("Employee 5")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 8)
                        .setHoursPerWeekRange(16, 24)
                        .addAvailability(Day.FRI, 8, 20)
                        .addAvailability(Day.SAT, 8, 20)
                        .addAvailability(Day.SUN, 8, 20)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee6")
                        .setName("Employee 6")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 8)
                        .setHoursPerWeekRange(24, 32)
                        .addAvailability(Day.MON, 8, 20)
                        .addAvailability(Day.FRI, 8, 20)
                        .addAvailability(Day.SAT, 8, 20)
                        .addAvailability(Day.SUN, 8, 20)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee7")
                        .setName("Employee 7")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 12)
                        .setHoursPerWeekRange(24, 32)
                        .addAvailability(Day.MON, 0, 20)
                        .addAvailability(Day.WED, 0, 20)
                        .addAvailability(Day.FRI, 0, 20)
                        .addAvailability(Day.SUN, 8, 16)
                        .build()
        );

        employees.add(
                new EmployeeBuilder()
                        .setUsername("employee8")
                        .setName("Employee 8")
                        .setPasswordEncoder(passwordEncoder)
                        .setCompany(company)
                        .setHoursPerDayRange(4, 12)
                        .setHoursPerWeekRange(20, 24)
                        .addAvailability(Day.THU, 0, 12)
                        .addAvailability(Day.FRI, 0, 12)
                        .addAvailability(Day.SAT, 0, 12)
                        .addAvailability(Day.SUN, 0, 12)
                        .build()
        );


        for (var employee : employees)
            employeeService.save(employee);
    }
}
