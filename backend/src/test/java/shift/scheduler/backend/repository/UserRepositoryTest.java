package shift.scheduler.backend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.builder.CompanyBuilder;
import shift.scheduler.backend.util.builder.EmployeeBuilder;
import shift.scheduler.backend.util.builder.ManagerBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFlushShouldSaveValidManager() throws Exception {

        var company = createSampleCompany();
        var manager = createSampleManager(company);

        userRepository.saveAndFlush(manager);

        Manager savedManager = (Manager) userRepository.findByAccountUsername(manager.getUsername())
                .orElseThrow();

        assertEquals(manager.getName(), savedManager.getName());
        assertEquals(manager.getCompany().getName(), savedManager.getCompany().getName());
        assertEquals(manager.getCompany().getLocation(), savedManager.getCompany().getLocation());
    }

    @Test
    void saveAndFlushShouldSaveValidEmployee() throws Exception {

        var company = createSampleCompany();
        var manager = createSampleManager(company);
        var employee = createSampleEmployee(company);

        userRepository.saveAndFlush(manager);
        userRepository.saveAndFlush(employee);

        Employee savedEmployee = (Employee) userRepository.findByAccountUsername(employee.getUsername())
                .orElseThrow();

        assertEquals(employee.getName(), savedEmployee.getName());
        assertEquals(employee.getCompany().getName(), savedEmployee.getCompany().getName());
        assertEquals(employee.getCompany().getLocation(), savedEmployee.getCompany().getLocation());
        assertEquals(employee.getMinHoursPerDay(), savedEmployee.getMinHoursPerDay());
        assertEquals(employee.getMaxHoursPerDay(), savedEmployee.getMaxHoursPerDay());
        assertEquals(employee.getMinHoursPerWeek(), savedEmployee.getMinHoursPerWeek());
        assertEquals(employee.getMaxHoursPerWeek(), savedEmployee.getMaxHoursPerWeek());
    }

    @ParameterizedTest
    @MethodSource("createUsersWithInvalidCompanies")
    void saveAndFlushShouldThrowExceptionWithInvalidCompanyDetails(User user) throws Exception {
        assertThrows(Exception.class, () -> userRepository.saveAndFlush(user));
    }

    @Test
    void deleteByAccountUsernameShouldDeletePersistedUser() throws Exception {

        var company = createSampleCompany();
        var manager = createSampleManager(company);
        var employee = createSampleEmployee(company);

        userRepository.saveAndFlush(manager);
        userRepository.saveAndFlush(employee);

        assertEquals(1, userRepository.deleteByAccountUsername(employee.getUsername()));
        assertFalse(userRepository.existsByAccountUsername(employee.getUsername()));

        assertEquals(1, userRepository.deleteByAccountUsername(manager.getUsername()));
        assertFalse(userRepository.existsByAccountUsername(manager.getUsername()));
    }

    static List<User> createUsersWithInvalidCompanies() {

        var manager = new ManagerBuilder()
                .setUsername("manager")
                .setPasswordEncoder(passwordEncoder)
                .setCompany(new Company())
                .build();

        var employee = new EmployeeBuilder()
                .setUsername("employee")
                .setPasswordEncoder(passwordEncoder)
                .setCompany(new Company())
                .build();

        return List.of(manager, employee);
    }

    static Company createSampleCompany() {
        return new CompanyBuilder()
                .setName("Company")
                .build();
    }

    static Manager createSampleManager(Company company) {
        return new ManagerBuilder()
                .setUsername("manager")
                .setPasswordEncoder(passwordEncoder)
                .setCompany(company)
                .build();
    }

    static Employee createSampleEmployee(Company company) {
        return new EmployeeBuilder()
                .setUsername("employee")
                .setPasswordEncoder(passwordEncoder)
                .setCompany(company)
                .setHoursPerWeekRange(4, 12)
                .setHoursPerDayRange(4, 12)
                .build();
    }
}
