package shift.scheduler.backend.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.dto.AccountDTO;
import shift.scheduler.backend.dto.CompanyDTO;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.util.Util;
import shift.scheduler.backend.util.exception.AuthenticationException;
import shift.scheduler.backend.util.exception.ErrorSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccountService accountService;

    @Mock
    UserService userService;

    @Mock
    CompanyService companyService;

    @InjectMocks
    AuthenticationService authenticationService = new AuthenticationService(new ModelMapper().registerModule(new RecordModule()));

    @Nested
    class Register {

        @Test
        void registerAsManagerShouldSucceedWithValidDetails() {

            mockAccountExistenceCheck(false);
            mockCompanyExistenceCheck(false);
            mockUserSave(Manager.class, true);
            mockJwtGeneration();

            var dto = createManagerRegistrationRequestDTO();

            var result = assertDoesNotThrow(() -> authenticationService.register(dto));
            assertEquals(Util.MOCK_JWT, result);
        }

        @Test
        void registerAsEmployeeShouldSucceedWithValidDetails() {

            mockAccountExistenceCheck(false);
            mockCompanyFindByNameAndLocation(true);
            mockUserSave(Employee.class, true);
            mockJwtGeneration();

            var dto = createEmployeeRegistrationRequestDTO();

            var result = assertDoesNotThrow(() -> authenticationService.register(dto));
            assertEquals(Util.MOCK_JWT, result);
        }

        @ParameterizedTest
        @MethodSource("createRegistrationRequestDTOs")
        void registerShouldFailWithTakenUsername(RegistrationRequestDTO dto) throws Exception {

            mockAccountExistenceCheck(true);
            assertThrows(AuthenticationException.class, () -> authenticationService.register(dto));
        }

        @Test
        void registerAsManagerShouldFailWithTakenCompanyNameAndLocation() throws Exception {

            mockAccountExistenceCheck(false);
            mockCompanyExistenceCheck(true);

            var dto = createManagerRegistrationRequestDTO();
            executeAndCheckForExceptionMessage(dto, "already exists");
        }

        @Test
        void registerAsEmployeeShouldFailWithNonexistentCompany() throws Exception {

            mockAccountExistenceCheck(false);
            mockCompanyFindByNameAndLocation(false);

            var dto = createEmployeeRegistrationRequestDTO();
            executeAndCheckForExceptionMessage(dto, "does not exist");
        }

        void executeAndCheckForExceptionMessage(RegistrationRequestDTO dto, String messageContent) throws Exception {

            var ex = assertThrows(AuthenticationException.class, () -> authenticationService.register(dto));
            assertEquals(ErrorSource.USER, ex.getSource());
            assertTrue(ex.getErrors() != null && !ex.getErrors().isEmpty());
            assertTrue(ex.getErrors().getFirst().contains(messageContent));
        }

        void mockAccountExistenceCheck(boolean isExistent) {
            when(accountService.exists(any())).thenReturn(isExistent);
        }

        void mockCompanyExistenceCheck(boolean isExistent) {
            when(companyService.exists(any(), any())).thenReturn(isExistent);
        }

        void mockCompanyFindByNameAndLocation(boolean isExistent) {
            Optional<Company> returnValue = isExistent ? Optional.of(new Company()) : Optional.empty();
            when(companyService.findByNameAndLocation(any(), any())).thenReturn(returnValue);
        }

        void mockUserSave(Class<? extends User> userClass, boolean isSuccessful) {
            if (userClass == Manager.class)
                when(userService.save(any())).thenReturn(isSuccessful ? Optional.of(new Manager()) : Optional.empty());
            else
                when(userService.save(any())).thenReturn(isSuccessful ? Optional.of(new Employee()) : Optional.empty());
        }

        void mockPasswordHashing() {
            when(passwordEncoder.encode(any())).thenReturn("");
        }

        void mockJwtGeneration() {
            when(jwtService.generateToken(any())).thenReturn(Util.MOCK_JWT);
        }

        static List<RegistrationRequestDTO> createRegistrationRequestDTOs() {
            return List.of(createManagerRegistrationRequestDTO(), createEmployeeRegistrationRequestDTO());
        }

        static RegistrationRequestDTO createManagerRegistrationRequestDTO() {

            return new RegistrationRequestDTO(
                    new AccountDTO("Name", "username", "password123", Role.MANAGER),
                    new CompanyDTO("Company", "City")
            );
        }

        static RegistrationRequestDTO createEmployeeRegistrationRequestDTO() {

            return new RegistrationRequestDTO(
                    new AccountDTO("Name", "username", "password123", Role.EMPLOYEE),
                    new CompanyDTO("Company", "City")
            );
        }
    }

    @Nested
    class Login {

        @Test
        void loginShouldSucceedWithValidUserDetails() throws Exception {

            mockFindByUsername(true);
            mockPasswordValidationResult(true);
            mockFindOrCreateJwt();

            var dto = createLoginRequestDTO();

            var result = assertDoesNotThrow(() -> authenticationService.login(dto));
            assertEquals(Util.MOCK_JWT, result);
        }

        @Test
        void loginShouldFailWithNonexistentUser() throws Exception {

            mockFindByUsername(false);

            var dto = createLoginRequestDTO();
            assertThrows(AuthenticationException.class, () -> authenticationService.login(dto));
        }

        @Test
        void loginShouldFailWithValidUsernameAndInvalidPassword() throws Exception {

            mockFindByUsername(true);
            mockPasswordValidationResult(false);

            var dto = createLoginRequestDTO();
            assertThrows(AuthenticationException.class, () -> authenticationService.login(dto));
        }

        private void mockFindByUsername(boolean isValid) {
            if (isValid)
                when(accountService.findByUsername(any())).thenReturn(Optional.of(new Account("username", "Name", "")));
            else
                when(accountService.findByUsername(any())).thenReturn(Optional.empty());
        }

        private void mockPasswordValidationResult(boolean isValid) {
            when(passwordEncoder.matches(any(), any())).thenReturn(isValid);
        }

        private void mockFindOrCreateJwt() {
            when(jwtService.findOrCreateToken(any())).thenReturn(Util.MOCK_JWT);
        }

        private LoginRequestDTO createLoginRequestDTO() {
            return new LoginRequestDTO("username", "password123");
        }
    }

    @Nested
    class Logout {

        @Test
        void logoutShouldSucceedWithValidJwt() {

            when(jwtService.deleteToken(any())).thenReturn(true);

            var header = createValidAuthHeaderWithJwt();

            assertDoesNotThrow(() -> authenticationService.logout(header));
        }

        @ParameterizedTest
        @MethodSource("createInvalidAuthHeaders")
        void logoutShouldFailDueToUserErrorWithInvalidAuthHeader(String header) throws Exception {

            var ex = assertThrows(AuthenticationException.class, () -> authenticationService.logout(header));

            assertEquals(ErrorSource.USER, ex.getSource());
            assertTrue(ex.getErrors() != null && !ex.getErrors().isEmpty());
            assertTrue(ex.getErrors().getFirst().contains("Invalid JWT"));
        }

        @Test
        void logoutReturnsServerErrorMessageUponTokenDeletionFailure() throws Exception {

            when(jwtService.deleteToken(any())).thenReturn(false);

            var header = createValidAuthHeaderWithJwt();
            var ex = assertThrows(AuthenticationException.class, () -> authenticationService.logout(header));

            assertEquals(ErrorSource.SERVER, ex.getSource());
            assertTrue(ex.getErrors() != null && !ex.getErrors().isEmpty());
        }

        static List<String> createInvalidAuthHeaders() {
            return List.of("", "Bearer", Util.MOCK_JWT, "Bearer test test");
        }

        static String createValidAuthHeaderWithJwt() {
            return String.format("Bearer %s", Util.MOCK_JWT);
        }
    }

    // TODO: Add getUserFromHeader tests
}
