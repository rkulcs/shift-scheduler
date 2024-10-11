package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shift.scheduler.backend.dto.EmployeeDashboardDataDTO;
import shift.scheduler.backend.dto.ErrorDTO;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.service.DashboardService;
import shift.scheduler.backend.service.UserService;
import shift.scheduler.backend.util.builder.EmployeeBuilder;
import shift.scheduler.backend.util.builder.EmployeeSettingsRequestJsonBuilder;
import shift.scheduler.backend.util.exception.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private DashboardService dashboardService;

    private final Employee sampleEmployee = new EmployeeBuilder()
            .setUsername("employee")
            .setPasswordEncoder(new BCryptPasswordEncoder())
            .setHoursPerDayRange(4, 8)
            .setHoursPerWeekRange(12, 48)
            .setCompany(new Company("Company", "City", new ArrayList<>()))
            .build();

    @Nested
    class Get implements EndpointTest {

        @Override
        public String endpoint() {
            return "/employee";
        }

        @Test
        void getShouldReturnBadRequestWhenNoEmployeeIsAssociatedWithJWT() throws Exception {

            var error = "Invalid username in JWT";

            when(userService.findByAuthHeaderValue(any()))
                    .thenThrow(new AuthenticationException(List.of(error)));

            var result = mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            var responseBody = deserialize(result.getResponse().getContentAsString(), ErrorDTO.class);

            assertTrue(responseBody.errors() != null && !responseBody.errors().isEmpty());
            assertEquals(error, responseBody.errors().getFirst());
        }

        @Test
        void getShouldReturnEmployeeWithValidRequest() throws Exception {

            when(userService.findByAuthHeaderValue(any()))
                    .thenReturn(sampleEmployee);

            var result = mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isOk())
                    .andReturn();

            assertDoesNotThrow(() -> deserialize(result.getResponse().getContentAsString(), Employee.class));
        }
    }

    @Nested
    class UpdateSettings implements EndpointTest {

        @Override
        public String endpoint() {
            return "/employee";
        }

        @ParameterizedTest
        @MethodSource("generateInvalidEmployeeSettingsRequestBodies")
        void updateSettingsShouldReturnBadRequestWithInvalidSettings(String json) throws Exception {
            var result = executeAuthenticatedPostWithUserError(sampleEmployee, endpoint(), json);
            assertFalse(result.errors() == null || result.errors().isEmpty());
        }

        @ParameterizedTest
        @MethodSource("generateValidEmployeeSettingsRequestBodies")
        void updateSettingsShouldReturnOkWithValidJWTAndSettings(String json) throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(sampleEmployee);
            when(userService.updateEmployeeSettings(any(), any())).thenReturn(true);

            executeAuthenticatedPostWithValidPayload(sampleEmployee, endpoint(), json);
        }

        static List<String> generateInvalidEmployeeSettingsRequestBodies() throws Exception {

            List<String> requestBodies = new ArrayList<>();

            var builder = new EmployeeSettingsRequestJsonBuilder();
            requestBodies.add(stringify(builder.build()));

            builder.setHoursPerDayRange(null, (short) 12);
            builder.setHoursPerWeekRange((short) 12, (short) 36);
            builder.addAvailability(Day.MON, (short) 4, (short) 16);
            requestBodies.add(stringify(builder.build()));

            builder.setHoursPerDayRange((short) 3, (short) 12);
            requestBodies.add(stringify(builder.build()));

            builder.setHoursPerDayRange((short) 12, (short) 4);
            requestBodies.add(stringify(builder.build()));

            builder.setHoursPerDayRange((short) 4, (short) 12);
            builder.addAvailability(Day.WED, (short) 36, (short) 48);
            requestBodies.add(stringify(builder.build()));

            builder.addAvailability(Day.TUE, (short) 12, (short) 4);
            requestBodies.add(stringify(builder.build()));

            return requestBodies;
        }

        static List<String> generateValidEmployeeSettingsRequestBodies() throws JsonProcessingException {

            List<String> requestBodies = new ArrayList<>();

            var builder = new EmployeeSettingsRequestJsonBuilder();

            requestBodies.add(stringify(builder
                    .setHoursPerDayRange((short) 4, (short) 12)
                    .setHoursPerWeekRange((short) 36, (short) 48)
                    .build()));

            requestBodies.add(stringify(builder
                    .setHoursPerDayRange((short) 4, (short) 4)
                    .addAvailability(Day.MON, (short) 8, (short) 16)
                    .build()));

            requestBodies.add(stringify(builder
                    .setHoursPerWeekRange((short) 12, (short) 48)
                    .addAvailability(Day.TUE, (short) 0, (short) 24)
                    .build()));

            return requestBodies;
        }
    }

    @Nested
    class Dashboard implements EndpointTest {

        @Override
        public String endpoint() {
            return "/employee/dashboard";
        }

        @Test
        void getDashboardShouldReturnOkWithValidUser() throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(sampleEmployee);
            when(dashboardService.getEmployeeDashboardData(any())).thenReturn(new EmployeeDashboardDataDTO(null, 0, 0));

            executeAuthenticatedGet(sampleEmployee, endpoint());
        }

        @Test
        void getDashboardShouldReturnNoContentForUserWithoutShifts() throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(sampleEmployee);
            when(dashboardService.getEmployeeDashboardData(any())).thenReturn(null);

            mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isNoContent())
                    .andReturn();
        }
    }
}
