package shift.scheduler.backend.util.builder;

import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Role;
import shift.scheduler.backend.model.period.Availability;
import shift.scheduler.backend.model.period.Day;

import java.util.ArrayList;
import java.util.Collection;

public class EmployeeBuilder extends UserBuilder<Employee, EmployeeBuilder> {

    protected Role role = Role.EMPLOYEE;

    private int minHoursPerDay;
    private int maxHoursPerDay;
    private int minHoursPerWeek;
    private int maxHoursPerWeek;

    private Collection<Availability> availabilities = new ArrayList<>();

    public EmployeeBuilder setHoursPerDayRange(int min, int max) {

        minHoursPerDay = min;
        maxHoursPerDay = max;
        return this;
    }

    public EmployeeBuilder setHoursPerWeekRange(int min, int max) {

        minHoursPerWeek = min;
        maxHoursPerWeek = max;
        return this;
    }

    public EmployeeBuilder addAvailability(Day day, int start, int end) {

        availabilities.add(new Availability((short) start, (short) end, day));
        return this;
    }

    @Override
    protected Employee createUser(Account account, Company company) {

        Employee employee = new Employee(
                account, company,
                (short) minHoursPerDay, (short) maxHoursPerDay,
                (short) minHoursPerWeek, (short) maxHoursPerWeek
        );

        employee.setAvailabilities(availabilities);

        return employee;
    }

    @Override
    protected Role getRole() {
        return Role.EMPLOYEE;
    }
}
