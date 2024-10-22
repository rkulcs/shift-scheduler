package shift.scheduler.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import shift.scheduler.backend.dto.AvailabilityDTO;
import shift.scheduler.backend.dto.EmployeeSettingsDTO;
import shift.scheduler.backend.dto.TimeIntervalDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.repository.UserRepository;
import shift.scheduler.backend.util.exception.AuthenticationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest extends ServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService(userRepository, jwtService, modelMapper);
    }

    @Nested
    class Exists {

        @Test
        void existsShouldReturnTrueIfUserWithGivenUsernameExists() throws Exception {
            when(userRepository.existsByAccountUsername(any())).thenReturn(true);
            assertTrue(userService.exists("username"));
        }

        @Test
        void existsShouldReturnFalseIfUserWithGivenUsernameDoesNotExist() throws Exception {
            when(userRepository.existsByAccountUsername(any())).thenReturn(false);
            assertFalse(userService.exists("username"));
        }
    }

    @Nested
    class Save {

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void saveShouldReturnSavedUserIfSuccessful(User user) throws Exception {
            assertTrue(userService.save(user).isPresent());
        }

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void saveShouldReturnEmptyUserIfUnsuccessful(User user) throws Exception {
            when(userRepository.saveAndFlush(any())).thenThrow(RuntimeException.class);
            assertTrue(userService.save(user).isEmpty());
        }
    }

    @Nested
    class FindByUsername {

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void findShouldReturnUserWithTakenUsername(User user) throws Exception {

            when(userRepository.findByAccountUsername(any())).thenReturn(Optional.of(user));

            var result = userService.findByUsername("username");
            assertTrue(result.isPresent());
            assertEquals(user, result.get());
        }

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void findShouldReturnEmptyUserWithUnusedUsername(User user) throws Exception {
            when(userRepository.findByAccountUsername(any())).thenReturn(Optional.empty());
            assertTrue(userService.findByUsername("username").isEmpty());
        }
    }

    @Nested
    class FindByAuthHeaderValue {

        @ParameterizedTest
        @MethodSource("shift.scheduler.backend.service.ServiceTest#getSampleUsers")
        void findShouldReturnUserWithValidAuthHeader(User user) throws Exception {
            when(userService.findByUsername(any())).thenReturn(Optional.of(user));
            assertEquals(user, userService.findByAuthHeaderValue("Bearer JWT"));
        }

        @Test
        void findShouldThrowAuthenticationExceptionWithInvalidAuthHeader() throws Exception {
            when(userService.findByUsername(any())).thenReturn(Optional.empty());
            assertThrows(AuthenticationException.class, () -> userService.findByAuthHeaderValue("Bearer JWT"));
        }
    }

    @Nested
    class UpdateEmployeeSettings {

        @Test
        void updateShouldReturnTrueIfEmployeeSettingsWereSaved() throws Exception {

            var settings = new EmployeeSettingsDTO(
                    new TimeIntervalDTO((short) 4, (short) 8),
                    new TimeIntervalDTO((short) 4, (short) 8),
                    List.of(new AvailabilityDTO(Day.MON, (short) 4, (short) 16))
            );

            assertTrue(userService.updateEmployeeSettings(sampleEmployee, settings));
        }

        @Test
        void updateShouldReturnFalseIfEmployeeSettingsWereNotSaved() throws Exception {

            var settings = new EmployeeSettingsDTO(
                    new TimeIntervalDTO((short) 4, (short) 8),
                    new TimeIntervalDTO((short) 4, (short) 7),
                    List.of()
            );

            when(userService.save(any())).thenReturn(Optional.empty());
            assertFalse(userService.updateEmployeeSettings(sampleEmployee, settings));
        }
    }
}
