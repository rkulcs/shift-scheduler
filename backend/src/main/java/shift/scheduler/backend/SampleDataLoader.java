package shift.scheduler.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.ManagerService;

import java.util.ArrayList;
import java.util.Collection;

@Component
@ConditionalOnProperty(name = "load.sample.data", havingValue = "true")
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
        Company company = createSampleManagerAndCompany();
        createSampleEmployees(company);
    }

    private Company createSampleManagerAndCompany() throws Exception {

        Company company = new Company("Company", "City", null);

        Collection<HoursOfOperation> hoursOfOperation = new ArrayList<>();
        hoursOfOperation.add(new HoursOfOperation((short) 4, (short) 20, null, Day.MON));
        hoursOfOperation.add(new HoursOfOperation((short) 4, (short) 20, null, Day.TUE));
        hoursOfOperation.add(new HoursOfOperation((short) 4, (short) 20, null, Day.WED));
        hoursOfOperation.add(new HoursOfOperation((short) 4, (short) 20, null, Day.THU));
        hoursOfOperation.add(new HoursOfOperation((short) 4, (short) 24, null, Day.FRI));
        hoursOfOperation.add(new HoursOfOperation((short) 0, (short) 24, null, Day.SAT));
        hoursOfOperation.add(new HoursOfOperation((short) 8, (short) 16, null, Day.SUN));

        company.setHoursOfOperation(hoursOfOperation);

        Account account = new Account("sampleManager", "Manager",
                passwordEncoder.encode("sampleManager"), Role.MANAGER);
        Manager manager = new Manager(account);
        manager.setCompany(company);

        managerService.save(manager);

        return company;
    }

    private void createSampleEmployees(Company company) throws Exception {

        Collection<Availability> availabilities = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            availabilities.add(new Availability((short) 4, (short) 20, Day.values()[i]));
        createEmployee(1, company, 8, 12, 32, 40, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 7; i++)
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
        for (int i = 4; i < 7; i++)
            availabilities.add(new Availability((short) 8, (short) 24, Day.values()[i]));
        createEmployee(7, company, 4, 8, 16, 24, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 3; i++)
            availabilities.add(new Availability((short) 16, (short) 24, Day.values()[i]));
        for (int i = 5; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(8, company, 8, 16, 40, 40, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 1; i < 2; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        for (int i = 5; i < 7; i++)
            availabilities.add(new Availability((short) 0, (short) 24, Day.values()[i]));
        createEmployee(9, company, 4, 8, 24, 28, availabilities);

        availabilities = new ArrayList<>();
        for (int i = 3; i < 6; i++)
            availabilities.add(new Availability((short) 0, (short) 16, Day.values()[i]));
        createEmployee(10, company, 4, 8, 24, 28, availabilities);
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
