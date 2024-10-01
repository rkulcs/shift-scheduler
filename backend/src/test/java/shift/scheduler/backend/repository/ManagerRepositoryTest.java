package shift.scheduler.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import shift.scheduler.backend.config.filter.JwtAuthenticationFilter;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.Role;
import shift.scheduler.backend.service.JwtService;
import shift.scheduler.backend.util.Util;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ManagerRepositoryTest {

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserRepository<Manager> managerRepository;

    @Test
    public void validManagerCreationShouldSucceed() throws Exception {

        Account account = Util.validAccounts[0];
        account.setRole(Role.MANAGER);

        Manager manager = new Manager(account);
        managerRepository.save(manager);

        assertThat(managerRepository.findByAccountUsername(account.getUsername())).isNotEmpty();
    }

    @Test
    public void invalidManagerCreationShouldFail() throws Exception {

        for (Account account : Util.invalidAccounts) {
            account.setRole(Role.MANAGER);
            Manager manager = new Manager(account);
            String username = manager.getUsername();

            try {
                managerRepository.save(manager);
            } catch (Exception e) {
                assertThat(managerRepository.findByAccountUsername(username)).isEmpty();
                return;
            }

            throw new Exception("Invalid account saved");
        }
    }
}
