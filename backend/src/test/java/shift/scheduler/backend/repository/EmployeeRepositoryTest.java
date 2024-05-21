package shift.scheduler.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import shift.scheduler.backend.config.WebSecurityConfig;
import shift.scheduler.backend.config.filter.JwtAuthenticationFilter;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Role;
import shift.scheduler.backend.service.JwtService;
import shift.scheduler.backend.util.Util;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeRepositoryTest {

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void validEmployeeCreationShouldSucceed() throws Exception {

        Account account = Util.validAccounts[0];
        account.setRole(Role.EMPLOYEE);

        Employee employee = new Employee(account);
        employeeRepository.save(employee);

        assertThat(employeeRepository.findByAccountUsername(account.getUsername())).isNotEmpty();
    }

    @Test
    public void invalidEmployeeCreationShouldFail() throws Exception {

        for (Account account : Util.invalidAccounts) {
            account.setRole(Role.EMPLOYEE);
            Employee employee = new Employee(account);
            String username = employee.getUsername();

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
