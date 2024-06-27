package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
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
}
