package shift.scheduler.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.util.Util;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void validEmployeeCreationShouldSucceed() throws Exception {

        Account account = Util.validAccounts[0];

        Employee employee = new Employee(account);
        employee.getAccount().setPasswordHash(Util.MOCK_HASH);
        employeeRepository.save(employee);

        assertThat(employeeRepository.findByAccountUsername(account.getUsername())).isNotEmpty();
    }

    @Test
    public void invalidEmployeeCreationShouldFail() throws Exception {

        for (Account account : Util.invalidAccounts) {
            Employee employee = new Employee(account);
            String username = employee.getUsername();

            if (employee.getAccount().getPassword() != null)
                employee.getAccount().setPasswordHash(Util.MOCK_HASH);

            try {
                employeeRepository.save(employee);
            } catch (Exception e) {
                assertThat(employeeRepository.findByAccountUsername(username)).isEmpty();
                return;
            }

            throw new Exception("Invalid account saved");
        }
    }
}
