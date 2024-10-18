package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import shift.scheduler.backend.dto.CompanyDashboardDataDTO;
import shift.scheduler.backend.dto.TimePeriodDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.DashboardService;
import shift.scheduler.backend.service.UserService;
import shift.scheduler.backend.util.Util;
import shift.scheduler.backend.util.exception.AuthenticationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class CompanyControllerTest extends ControllerTest {

    private static final Manager manager = Util.createValidManager();
    private static final Company company = mock(Company.class);

    @MockBean
    private UserService userService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private DashboardService dashboardService;

    @Nested
    class GetAll implements EndpointTest {

        @Override
        public String endpoint() {
            return "/company/all";
        }

        @Test
        void getAllShouldReturnNoContentIfCompanyListIsNull() throws Exception {

            when(companyService.findAll()).thenReturn(null);

            mockMvc.perform(get(endpoint()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void getAllShouldReturnNoContentIfCompanyListIsEmpty() throws Exception {

            when(companyService.findAll()).thenReturn(new ArrayList<>());

            mockMvc.perform(get(endpoint()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void getAllShouldReturnOkIfCompanyListIsNonempty() throws Exception {

            when(companyService.findAll()).thenReturn(List.of(Util.company));

            mockMvc.perform(get(endpoint()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class Get implements EndpointTest {

        @Override
        public String endpoint() {
            return "/company";
        }

        @Test
        void getShouldReturnBadRequestWithInvalidUser() throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenThrow(AuthenticationException.class);

            mockMvc.perform(get(endpoint()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getShouldReturnNoContentIfUserHasNoCompany() throws Exception {

            Manager manager = mock(Manager.class);

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);
            when(manager.getCompany()).thenReturn(null);

            mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isNoContent());
        }

        @Test
        void getShouldReturnOkWithValidUser() throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);

            mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class SetHours implements EndpointTest {

        @Override
        public String endpoint() {
            return "/company/hours";
        }

        @ParameterizedTest
        @MethodSource("createValidSetCompanyHoursRequests")
        void setHoursShouldReturnBadRequestWithInvalidManager(String hours) throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenThrow(new AuthenticationException(List.of("Invalid user")));

            mockMvc.perform(postJson(endpoint(), hours).header("Authorization", ""))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("createInvalidSetCompanyHoursRequests")
        void setHoursShouldReturnBadRequestWithInvalidHours(String hours) throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);

            mockMvc.perform(postJson(endpoint(), hours).header("Authorization", ""))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("createValidSetCompanyHoursRequests")
        void setHoursShouldReturnOkWithValidInput(String hours) throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);
            when(companyService.updateHoursOfOperation(any(), any())).thenReturn(true);

            mockMvc.perform(postJson(endpoint(), hours)
                                    .header("Authorization", ""))
                    .andExpect(status().isOk());
        }

        static List<String> createValidSetCompanyHoursRequests() throws JsonProcessingException {

            List<String> requestBodies = new ArrayList<>();

            for (int i = 4; i <= 24; i += 4) {
                int end = i;
                requestBodies.add(stringify(
                        Arrays.stream(Day.values()).map(day -> new TimePeriodDTO(day, (short) 0, (short) end)).toList()
                ));
            }

            requestBodies.add(stringify(List.of(new TimePeriodDTO(Day.TUE, (short) 8, (short) 16))));

            return requestBodies;
        }

        static List<String> createInvalidSetCompanyHoursRequests() throws JsonProcessingException {

            List<String> requestBodies = new ArrayList<>();

            requestBodies.add(stringify(List.of(new TimePeriodDTO(Day.MON, (short) 0, (short) 60))));
            requestBodies.add(stringify(List.of(
                    new TimePeriodDTO(Day.MON, (short) 0, (short) 16),
                    new TimePeriodDTO(Day.TUE, (short) 4, (short) 0)
            )));
            requestBodies.add(stringify(List.of(new TimePeriodDTO(Day.MON, (short) 0, null))));
            requestBodies.add(stringify(List.of(new TimePeriodDTO(Day.MON, null, null))));
            requestBodies.add(stringify(List.of(new TimePeriodDTO(Day.MON, null, (short) 8))));
            requestBodies.add(stringify(List.of(
                    new TimePeriodDTO(Day.MON, (short) 0, (short) 16),
                    new TimePeriodDTO(Day.TUE, (short) 4, (short) 0),
                    new TimePeriodDTO(Day.WED, (short) 0, (short) 16)
            )));
            requestBodies.add(stringify(List.of(
                    new TimePeriodDTO(Day.MON, (short) 0, (short) 16),
                    new TimePeriodDTO(Day.WED, (short) 0, (short) 7)
            )));

            return requestBodies;
        }
    }

    @Nested
    class GetDashboard implements EndpointTest {

        @Override
        public String endpoint() {
            return "/company/dashboard";
        }

        @Test
        void getDashboardShouldReturnBadRequestWithInvalidUser() throws Exception {

            when(userService.findByAuthHeaderValue(any())).thenThrow(new AuthenticationException(List.of("Invalid user")));

            mockMvc.perform(get(endpoint()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getDashboardShouldReturnNoContentWithValidUserAndNonexistentSchedules() throws Exception {

            when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
            when(dashboardService.getCompanyDashboardData(any())).thenReturn(null);

            mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isNoContent());
        }

        @Test
        void getDashboardShouldReturnOkWithValidUserAndExistingSchedules() throws Exception {

            var data = new CompanyDashboardDataDTO(
                    new CompanyDashboardDataDTO.DailyScheduleSummary(
                            LocalDate.of(2024, 1, 1), (short) 4, (short) 24
                    ),
                    5,
                    80
            );

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);
            when(dashboardService.getCompanyDashboardData(any())).thenReturn(data);

            mockMvc.perform(get(endpoint()).header("Authorization", ""))
                    .andExpect(status().isOk());
        }
    }

}
