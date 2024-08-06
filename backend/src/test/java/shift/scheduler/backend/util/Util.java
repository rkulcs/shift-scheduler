package shift.scheduler.backend.util;

import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.dto.ScheduleGenerationRequestDTO;

import java.time.LocalDate;
import java.util.List;

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

    public static final ScheduleGenerationRequestDTO VALID_SCHEDULE_GENERATION_REQUEST_DTO = new ScheduleGenerationRequestDTO(
            LocalDate.now(), (short) 2
    );

    public static Employee createValidEmployee() {
        Employee employee = new Employee(
                validAccounts[0],
                company,
                (short) 4, (short) 8, (short) 12, (short) 24
        );

        return employee;
    }

    public static Manager createValidManager() {
        Manager manager = new Manager(validAccounts[0]);
        manager.setCompany(company);

        return manager;
    }

    public static ScheduleForWeek createValidScheduleForWeek() {
        ScheduleForDay dailySchedule = new ScheduleForDay(
                Day.MON, List.of(new Shift((short) 4, (short) 8, createValidEmployee()))
        );

        ScheduleForWeek schedule = new ScheduleForWeek(List.of(dailySchedule));
        schedule.setCompany(company);

        return schedule;
    }
}
