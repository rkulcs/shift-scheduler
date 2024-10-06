package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
import shift.scheduler.backend.model.period.TimeInterval;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.model.violation.CompanyConstraintViolation;
import shift.scheduler.backend.model.violation.EmployeeConstraintViolation;
import shift.scheduler.backend.util.Util;

import static org.assertj.core.api.Assertions.assertThat;

public class ScheduleConstraintViolationTest {

    @Test
    public void producesCorrectEmployeeConstraintViolationString() throws Exception {

        Account account = Util.validAccounts[0];
        Employee employee = new Employee(account);

        var violation = new EmployeeConstraintViolation(
                employee,
                EmployeeConstraintViolation.Type.DAILY_HOURS,
                -3
        );

        assertThat(violation.toString())
                .isEqualTo(String.format("%s's minimum daily hours subceeded by 3 hours.", account.getName()));

        violation = new EmployeeConstraintViolation(
                employee,
                EmployeeConstraintViolation.Type.DAILY_HOURS,
                3
        );

        assertThat(violation.toString())
                .isEqualTo(String.format("%s's maximum daily hours exceeded by 3 hours.", account.getName()));

        violation = new EmployeeConstraintViolation(
                employee,
                EmployeeConstraintViolation.Type.WEEKLY_HOURS,
                -3
        );

        assertThat(violation.toString())
                .isEqualTo(String.format("%s's minimum weekly hours subceeded by 3 hours.", account.getName()));

        violation = new EmployeeConstraintViolation(
                employee,
                EmployeeConstraintViolation.Type.WEEKLY_HOURS,
                3
        );

        assertThat(violation.toString())
                .isEqualTo(String.format("%s's maximum weekly hours exceeded by 3 hours.", account.getName()));
    }

    @Test
    public void producesCorrectCompanyConstraintViolationString() throws Exception {

        TimeInterval period = new TimePeriod((short) 4, (short) 8);

        var violation = new CompanyConstraintViolation(period, 1);
        assertThat(violation.toString())
                .isEqualTo("Required number of employees between 4:00 and 8:00 exceeded by 1.");

        violation = new CompanyConstraintViolation(period, -2);
        assertThat(violation.toString())
                .isEqualTo("Required number of employees between 4:00 and 8:00 subceeded by 2.");
    }
}
