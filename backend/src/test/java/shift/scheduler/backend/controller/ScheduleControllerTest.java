package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.dto.ScheduleGenerationRequestDTO;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.service.ScheduleGenerationService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ScheduleControllerTest extends ControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    ScheduleGenerationService scheduleGenerationService;

    @Nested
    class GetSchedule implements EndpointTest {

        @Override
        public String endpoint() {
            return "/schedule";
        }

        public String endpoint(String date) {
            return String.format("%s/%s", endpoint(), date);
        }

        @BeforeEach
        void beforeEach() {

            Employee employee = new Employee();
            employee.setCompany(new Company());

            when(userService.findByAuthHeaderValue(any())).thenReturn(employee);
        }

        @ParameterizedTest
        @MethodSource("createValidDates")
        void getShouldReturnOkWithValidDateAndExistingSchedule(String date) throws Exception {

            var schedule = new ScheduleForWeek(new ArrayList<>());
            when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.of(schedule));

            mockMvc.perform(get(endpoint(date)).header("Authorization", ""))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("createValidDates")
        void getShouldReturnUnprocessableEntityWithValidDateAndNonexistentSchedule(String date) throws Exception {

            when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.empty());

            mockMvc.perform(get(endpoint(date)).header("Authorization", ""))
                    .andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @MethodSource("createInvalidDates")
        void getShouldReturnBadRequestWithInvalidDate(String date) throws Exception {

            mockMvc.perform(get(endpoint(date)).header("Authorization", ""))
                    .andExpect(status().isBadRequest());
        }

        private static List<String> createValidDates() {

            return List.of(
                "2024-01-01",
                "2023-01-30",
                "2023-02-28",
                "2023-12-31"
            );
        }

        private static List<String> createInvalidDates() {

            return List.of(
                "2024",
                "2023-01",
                "2023-02-31",
                "date"
            );
        }
    }

    @Nested
    class GenerateSchedule implements EndpointTest {

        @Override
        public String endpoint() {
            return "/schedule/generate";
        }

        @ParameterizedTest
        @MethodSource("createInvalidScheduleGenerationRequests")
        void generateShouldReturnBadRequestWithInvalidRequest(String request) throws Exception {

            Manager manager = new Manager();
            manager.setCompany(new Company());

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);

            mockMvc.perform(
                    post(endpoint())
                            .contentType("application/json")
                            .content(request)
                            .header("Authorization", "")
            ).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("createValidScheduleGenerationRequests")
        void generateShouldReturnUnprocessableEntityWithValidRequestIfNoScheduleWasGenerated(String request) throws Exception {

            Manager manager = new Manager();
            manager.setCompany(new Company());

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);

            mockMvc.perform(
                    post(endpoint())
                            .contentType("application/json")
                            .content(request)
                            .header("Authorization", "")
            ).andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @MethodSource("createValidScheduleGenerationRequests")
        void generateShouldReturnOkWithValidRequestIfSchedulesWereGenerated(String request) throws Exception {

            var manager = new Manager();
            var company = new Company();
            manager.setCompany(company);

            var schedule = new ScheduleForWeek(new ArrayList<>());
            schedule.setCompany(company);

            when(userService.findByAuthHeaderValue(any())).thenReturn(manager);
            when(scheduleGenerationService.generateSchedulesForWeek(any(), any()))
                    .thenReturn(List.of(schedule));

            mockMvc.perform(
                    post(endpoint())
                            .contentType("application/json")
                            .content(request)
                            .header("Authorization", "")
            ).andExpect(status().isOk());
        }

        private static List<String> createInvalidScheduleGenerationRequests() throws JsonProcessingException {

            return List.of(
                    stringify(new ScheduleGenerationRequestDTO(null, null)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2025, 2, 1), null)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 1, 1), null)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 1, 1), (short) -1)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 1, 1), (short) 0))
            );
        }

        private static List<String> createValidScheduleGenerationRequests() throws JsonProcessingException {

            return List.of(
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 1, 1), (short) 1)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 1, 1), (short) 4)),
                    stringify(new ScheduleGenerationRequestDTO(LocalDate.of(2024, 12, 31), (short) 10))
            );
        }
    }

    @Nested
    class SaveSchedule implements EndpointTest {

        @Override
        public String endpoint() {
            return "/schedule";
        }

        @Test
        void saveShouldReturnInternalServerErrorIfScheduleCouldNotBeSaved() throws Exception {

            var schedule = new ScheduleForWeek(new ArrayList<>());
            schedule.setCompany(new Company());

            when(scheduleService.save(any())).thenReturn(Optional.empty());

            mockMvc.perform(
                    post(endpoint())
                            .contentType("application/json")
                            .content(stringify(schedule))
                            .header("Authorization", "")
            ).andExpect(status().isInternalServerError());
        }

        @Test
        void saveShouldReturnOkIfScheduleWasSaved() throws Exception {

            var schedule = new ScheduleForWeek(new ArrayList<>());
            schedule.setCompany(new Company());

            when(scheduleService.save(any())).thenReturn(Optional.of(schedule));

            mockMvc.perform(
                    post(endpoint())
                            .contentType("application/json")
                            .content(stringify(schedule))
                            .header("Authorization", "")
            ).andExpect(status().isOk());
        }
    }
}
