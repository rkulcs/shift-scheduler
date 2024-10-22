package shift.scheduler.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.AccountRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceTest extends ServiceTest {

    @Mock
    AccountRepository accountRepository;

    AccountService accountService;

    @BeforeEach
    void beforeEach() {
        accountService = new AccountService(accountRepository);
    }

    @Nested
    class Exists {

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void existsShouldReturnTrueIfAccountExists(User user) throws Exception {
            when(accountRepository.findById(any())).thenReturn(Optional.of(user.getAccount()));
            assertTrue(accountService.exists(user.getUsername()));
        }

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void existsShouldReturnFalseIfAccountDoesNotExist(User user) throws Exception {
            when(accountRepository.findById(any())).thenReturn(Optional.empty());
            assertFalse(accountService.exists(user.getUsername()));
        }
    }

    @Nested
    class FindByUsername {

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void shouldReturnAccountIfAccountWithGivenUsernameExists(User user) throws Exception {
            when(accountRepository.findById(user.getUsername())).thenReturn(Optional.of(user.getAccount()));
            assertEquals(user.getAccount(), accountService.findByUsername(user.getUsername()).get());
        }

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void shouldReturnEmptyAccountIfAccountWithGivenUsernameDoesNotExist(User user) throws Exception {
            when(accountRepository.findById(user.getUsername())).thenReturn(Optional.empty());
            assertTrue(accountService.findByUsername(user.getUsername()).isEmpty());
        }
    }
}
