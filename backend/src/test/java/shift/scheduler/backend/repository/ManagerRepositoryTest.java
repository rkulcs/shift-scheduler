package shift.scheduler.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.util.Util;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ManagerRepositoryTest {

    @Autowired
    ManagerRepository managerRepository;

    @Test
    public void validManagerCreationShouldSucceed() throws Exception {

        Account account = Util.validAccounts[0];

        Manager manager = new Manager(account);
        manager.getAccount().setPasswordHash(Util.MOCK_HASH);
        managerRepository.save(manager);

        assertThat(managerRepository.findByAccountUsername(account.getUsername())).isNotEmpty();
    }

    @Test
    public void invalidManagerCreationShouldFail() throws Exception {

        for (Account account : Util.invalidAccounts) {
            Manager manager = new Manager(account);
            String username = manager.getUsername();

            if (manager.getAccount().getPassword() != null)
                manager.getAccount().setPasswordHash(Util.MOCK_HASH);

            try {
                managerRepository.save(manager);
            } catch (Exception e) {
            }

            assertThat(managerRepository.findByAccountUsername(username)).isEmpty();
        }
    }
}
