package shift.scheduler.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.util.EntityValidationException;
import shift.scheduler.backend.util.Util;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AccountService accountService;

    @BeforeEach
    public void initMocks() {
        when(passwordEncoder.encode(anyString())).thenReturn(Util.MOCK_HASH);
    }

    @Test
    public void invalidPasswordsShouldNotBeHashed() throws Exception {

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("test", "Test", "123"));
        accounts.add(new Account("test", "Test", null));
        accounts.add(new Account("test", "Test", "abcd123"));

        for (Account account : accounts) {
            try {
                accountService.hashPassword(account);
                throw new Exception("Invalid password hashed by service");
            } catch (Exception e) {
                assertThat(e).isInstanceOf(EntityValidationException.class);
            }
        }
    }

    @Test
    public void validPasswordsShouldBeHashed() throws Exception {

        for (Account account : Util.validAccounts) {
            try {
                accountService.hashPassword(account);
                assertThat(account.getPasswordHash()).isEqualTo(Util.MOCK_HASH);
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
