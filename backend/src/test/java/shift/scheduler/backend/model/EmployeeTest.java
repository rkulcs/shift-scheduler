package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.TimePeriod;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    @Test
    void allPossibleShiftsAreGenerated() throws Exception {

        Employee employee = createEmployee();
        TimePeriod period = new TimePeriod(Day.MON, (short) 4, (short) 20);

        Collection<Shift> shifts = employee.generatePotentialShifts(period);
        assertThat(shifts.size()).isEqualTo(9);
    }

    @Test
    void noShiftsAreGeneratedIfNotAvailableOnGivenDay() {

        Employee employee = createEmployee();
        TimePeriod period = new TimePeriod(Day.TUE, (short) 4, (short) 20);

        Collection<Shift> shifts = employee.generatePotentialShifts(period);
        assertThat(shifts).isNull();
    }

    private Employee createEmployee() {

        Employee employee = new Employee(
                new Account("employee", "Employee", "employee"), null,
                (short) 4, (short) 12, (short) 4, (short) 4
        );

        Collection<TimePeriod> availabilities = new ArrayList<>();
        availabilities.add(new TimePeriod(Day.MON, (short) 4, (short) 24));
        employee.setAvailabilities(availabilities);

        return employee;
    }
}
