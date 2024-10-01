package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
import shift.scheduler.backend.model.period.Availability;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.HoursOfOperation;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    @Test
    void allPossibleShiftsAreGenerated() throws Exception {

        Employee employee = createEmployee();
        HoursOfOperation period = new HoursOfOperation((short) 4, (short) 20, null, Day.MON);

        Collection<Shift> shifts = employee.generatePotentialShifts(period);
        assertThat(shifts.size()).isEqualTo(9);
    }

    @Test
    void noShiftsAreGeneratedIfNotAvailableOnGivenDay() {

        Employee employee = createEmployee();
        HoursOfOperation period = new HoursOfOperation((short) 4, (short) 20, null, Day.TUE);

        Collection<Shift> shifts = employee.generatePotentialShifts(period);
        assertThat(shifts).isNull();
    }

    private Employee createEmployee() {

        Employee employee = new Employee(
                new Account("employee", "Employee", "employee"), null,
                (short) 4, (short) 12, (short) 4, (short) 4
        );

        Collection<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((short) 4, (short) 24, Day.MON));
        employee.setAvailabilities(availabilities);

        return employee;
    }
}
