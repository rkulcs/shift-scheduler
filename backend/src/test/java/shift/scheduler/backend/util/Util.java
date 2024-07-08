package shift.scheduler.backend.util;

import org.checkerframework.checker.units.qual.A;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

public final class Util {

    public static final String MOCK_HASH = "hash";
    public static final String MOCK_JWT = "token";

    public static final Account[] validAccounts = {
            new Account("user", "Test User 1", "password"),
            new Account("user2", "Test User 2", "password"),
    };

    public static final Account[] invalidAccounts = {
            new Account("", "Test User", "password"),
            new Account("username", "", "password"),
            new Account("username", "Test User", null)
    };

    public static final Company company = new Company("Company", "City", null);

    public static final ScheduleGenerationRequest validScheduleGenerationRequest = new ScheduleGenerationRequest(
            LocalDate.now(), (short) 2
    );

    public static Employee createValidEmployee() {
        Employee employee = new Employee(
                validAccounts[0],
                company,
                (short) 0, (short) 0, (short) 0, (short) 0
        );

        return employee;
    }

    public static Manager createValidManager() {
        Manager manager = new Manager(validAccounts[0]);
        manager.setCompany(company);

        return manager;
    }

    public static ScheduleForWeek createValidScheduleForWeek() {
        ScheduleForWeek schedule = new ScheduleForWeek(List.of(new ScheduleForDay()));
        schedule.setCompany(company);

        return schedule;
    }
}
