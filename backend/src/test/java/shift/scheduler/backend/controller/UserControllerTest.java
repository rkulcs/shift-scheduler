package shift.scheduler.backend.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import shift.scheduler.backend.dto.ErrorDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.util.Util;
import shift.scheduler.backend.util.builder.RegistrationRequestJsonBuilder;
import shift.scheduler.backend.util.exception.AuthenticationException;
import shift.scheduler.backend.util.exception.ErrorSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class UserControllerTest extends ControllerTest {

    @Nested
    class Registration implements EndpointTest {

        @Override
        public String endpoint() {
            return "/user/register";
        }

        @ParameterizedTest
        @MethodSource("generateRegistrationRequestBodiesWithMissingDetails")
        void registrationShouldFailWithMissingDetails(String json) throws Exception {

            var responseBody = executeUnauthenticatedPostWithUserError(endpoint(), json);

            assertNotNull(responseBody.errors());
            assertFalse(responseBody.errors().isEmpty());
        }

        @ParameterizedTest
        @MethodSource("genereateRegistrationRequestBodiesWithBlankDetails")
        void registrationShouldFailWithBlankDetails(String json) throws Exception {

            var responseBody = executeUnauthenticatedPostWithUserError(endpoint(), json);

            assertNotNull(responseBody.errors());
            assertFalse(responseBody.errors().isEmpty());
        }

        @ParameterizedTest
        @MethodSource("generateRegistrationRequestBodiesWithInvalidPasswords")
        void registrationShouldFailWithInvalidPassword(String json) throws Exception {

            var responseBody = executeUnauthenticatedPostWithUserError(endpoint(), json);

            assertNotNull(responseBody.errors());
            assertEquals(1, responseBody.errors().size());
        }

        @ParameterizedTest
        @MethodSource("generateValidRegistrationRequestBodies")
        void registrationShouldSucceedWithValidDetails(String json) throws Exception {

            mockMvc.perform(postJson(endpoint(), json))
                    .andExpect(status().isOk());
        }

        private static List<String> generateRegistrationRequestBodiesWithMissingDetails() {

            List<String> requestBodies = new ArrayList<>();

            var builder = new RegistrationRequestJsonBuilder();

            requestBodies.add(builder.build());
            requestBodies.add(builder.setName("Name").build());
            requestBodies.add(builder.setCompanyName("Company").build());
            requestBodies.add(builder.setUsername("Name").build());
            requestBodies.add(builder.setPassword("password123").build());
            requestBodies.add(builder.setCompanyLocation("City").build());

            return requestBodies;
        }

        private static List<String> genereateRegistrationRequestBodiesWithBlankDetails() {

            List<String> requestBodies = new ArrayList<>();

            var builder = new RegistrationRequestJsonBuilder()
                    .setName("")
                    .setUsername("Name")
                    .setPassword("password123")
                    .setRole(Role.MANAGER)
                    .setCompanyName("Company")
                    .setCompanyLocation("City");

            requestBodies.add(builder.build());

            builder.setName("Name").setUsername("");
            requestBodies.add(builder.build());

            builder.setUsername("Name").setCompanyName("");
            requestBodies.add(builder.build());

            builder.setCompanyName("Company").setCompanyLocation("");
            requestBodies.add(builder.build());

            return requestBodies;
        }

        private static List<String> generateRegistrationRequestBodiesWithInvalidPasswords() {

            List<String> requestBodies = new ArrayList<>();

            var builder = new RegistrationRequestJsonBuilder()
                    .setName("Name")
                    .setUsername("Name")
                    .setRole(Role.MANAGER)
                    .setCompanyName("Company")
                    .setCompanyLocation("City");

            var passwordBuilder = new StringBuilder();

            for (int i = 1; i < 8; i++) {
                passwordBuilder.append("x");
                requestBodies.add(builder.setPassword(passwordBuilder.toString()).build());
            }

            return requestBodies;
        }

        private static List<String> generateValidRegistrationRequestBodies() {

            var managerJson = new RegistrationRequestJsonBuilder()
                    .setName("Manager")
                    .setUsername("manager")
                    .setPassword("password123")
                    .setRole(Role.MANAGER)
                    .setCompanyName("Company")
                    .setCompanyLocation("City")
                    .build();

            var employeeJson = new RegistrationRequestJsonBuilder()
                    .setName("Employee")
                    .setUsername("employee")
                    .setPassword("password123")
                    .setRole(Role.EMPLOYEE)
                    .setCompanyName("Company")
                    .setCompanyLocation("City")
                    .build();

            return List.of(managerJson, employeeJson);
        }
    }

    @Nested
    class Login implements EndpointTest {

        @Override
        public String endpoint() {
            return "/user/login";
        }

        @ParameterizedTest
        @ValueSource(strings = { "Invalid username", "Invalid password" })
        void LoginShouldFailWithInvalidUsernameOrPassword(String error) throws Exception {

            when(authenticationService.login(any())).thenThrow(new AuthenticationException(List.of(error)));

            String json = createLoginRequestBody();

            var responseBody = executeUnauthenticatedPostWithUserError(endpoint(), json);

            assertNotNull(responseBody.errors());
            assertFalse(responseBody.errors().isEmpty());
            assertEquals(error, responseBody.errors().getFirst());
        }

        @Test
        void loginShouldSucceedWithValidUsernameAndPassword() throws Exception {

            when(authenticationService.login(any())).thenReturn(Util.MOCK_JWT);

            String json = createLoginRequestBody();

            mockMvc.perform(postJson("/user/login", json))
                    .andExpect(status().isOk());
        }

        private String createLoginRequestBody() throws Exception {

            var requestBody = new HashMap<String, String>();
            requestBody.put("username", "username");
            requestBody.put("password", "password123");

            return stringify(requestBody);
        }
    }

    @Nested
    class Logout implements EndpointTest {

        @Override
        public String endpoint() {
            return "/user/logout";
        }

        @Test
        void logoutShouldSucceedWithValidTokenInAuthorizationHeader() throws Exception {
            mockMvc.perform(post(endpoint()).header("Authorization", ""))
                    .andExpect(status().isOk());
        }

        @Test
        void logoutShouldFailWithInvalidTokenInAuthorizationHeader() throws Exception {

            var error = "Invalid JWT token in authorization header";

            doThrow(new AuthenticationException(List.of(error)))
                    .when(authenticationService).logout(any());

            var result = mockMvc.perform(post(endpoint()).header("Authorization", ""))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            var responseBody = deserialize(result.getResponse().getContentAsString(), ErrorDTO.class);

            assertNotNull(responseBody.errors());
            assertFalse(responseBody.errors().isEmpty());
            assertEquals(error, responseBody.errors().getFirst());
        }

        @Test
        void logoutFailureDueToServerErrorShouldReturnInternalServerError() throws Exception {

            var error = "Failed to log out";

            doThrow(new AuthenticationException(List.of(error), ErrorSource.SERVER))
                    .when(authenticationService).logout(any());

            var result = mockMvc.perform(post(endpoint()).header("Authorization", ""))
                    .andExpect(status().isInternalServerError())
                    .andReturn();

            var responseBody = deserialize(result.getResponse().getContentAsString(), ErrorDTO.class);

            assertNotNull(responseBody.errors());
            assertFalse(responseBody.errors().isEmpty());
            assertEquals(error, responseBody.errors().getFirst());
        }
    }
}
