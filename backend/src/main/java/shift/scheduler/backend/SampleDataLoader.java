package shift.scheduler.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.Availability;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.HoursOfOperation;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.ManagerService;
import shift.scheduler.backend.util.builder.CompanyBuilder;
import shift.scheduler.backend.util.builder.ManagerBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Component
@Profile("dev")
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

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

        Collection<Availability> availabilities = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            availabilities.add(new Availability((short) 4, (short) 20, Day.values()[i]));
        createEmployee(1, company, 8, 12, 32, 40, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            availabilities.add(new Availability((short) 8, (short) 24, Day.values()[i]));
        createEmployee(2, company, 4, 12, 36, 48, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 3; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 12, Day.values()[i]));
        createEmployee(3, company, 4, 8, 12, 24, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(4, company, 4, 8, 24, 36, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 4; i++)
            availabilities.add(new Availability((short) 16, (short) 24, Day.values()[i]));
        createEmployee(5, company, 4, 4, 8, 12, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 4; i < 7; i++)
            availabilities.add(new Availability((short) 16, (short) 24, Day.values()[i]));
        createEmployee(6, company, 4, 4, 8, 12, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            availabilities.add(new Availability((short) 8, (short) 24, Day.values()[i]));
        createEmployee(7, company, 4, 8, 16, 24, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 3; i++)
            availabilities.add(new Availability((short) 16, (short) 24, Day.values()[i]));
        for (int i = 5; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(8, company, 8, 16, 40, 40, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        for (int i = 5; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(9, company, 4, 8, 24, 28, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 3; i < 6; i++)
            availabilities.add(new Availability((short) 0, (short) 16, Day.values()[i]));
        createEmployee(10, company, 4, 8, 24, 28, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        for (int i = 5; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(11, company, 8, 12, 24, 28, availabilities);
    }

    private void createEmployee(
            int num,
            Company company,
            int minHoursPerDay, int maxHoursPerDay, int minHoursPerWeek, int maxHoursPerWeek,
            Collection<Availability> availabilities) throws Exception {

        Account account = new Account(
                "sampleEmployee" + num,
                "Sample Employee " + num,
                passwordEncoder.encode("sampleEmployee" + num),
                Role.EMPLOYEE);
        Employee employee = new Employee(
                account, company,
                (short) minHoursPerDay, (short) maxHoursPerDay, (short) minHoursPerWeek, (short) maxHoursPerWeek
        );

        employee.setAvailabilities(availabilities);
        employeeService.save(employee);
    }
}
