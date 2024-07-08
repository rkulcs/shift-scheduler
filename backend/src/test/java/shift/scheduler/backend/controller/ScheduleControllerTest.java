package shift.scheduler.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.config.WebSecurityConfig;
import shift.scheduler.backend.config.filter.JwtAuthenticationFilter;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.payload.ScheduleGenerationRequest;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.JwtService;
import shift.scheduler.backend.service.ScheduleGenerationService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.util.Util;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ScheduleControllerTest {

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    WebSecurityConfig webSecurityConfig;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    ScheduleGenerationService scheduleGenerationService;

    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getScheduleShouldReturnBadRequestWithInvalidUser() throws Exception {

        when(authenticationService.getUserFromHeader(any())).thenReturn(null);

        mockMvc.perform(
                get("/schedule/2024-06-30").header("Authorization", "Bearer jwt")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getScheduleShouldReturnBadRequestWithInvalidCompany() throws Exception {

        Employee employee = new Employee();

        mockMvc.perform(
                get("/schedule/2024-06-30").header("Authorization", "Bearer jwt")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getScheduleShouldReturnBadRequestWithInvalidDate() throws Exception {

        Employee employee = new Employee();
        employee.setCompany(new Company());

        when(authenticationService.getUserFromHeader(any())).thenReturn(employee);

        mockMvc.perform(
                get("/schedule/date").header("Authorization", "Bearer jwt")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getScheduleShouldReturnOkWithValidUserAndDate() throws Exception {

        Employee employee = Util.createValidEmployee();

        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(authenticationService.getUserFromHeader(any())).thenReturn(employee);
        when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Util.createValidScheduleForWeek());

        mockMvc.perform(
                get("/schedule/2024-06-30").header("Authorization", "Bearer jwt")
        ).andExpect(status().isOk());
    }

    @Test
    void scheduleGenerationShouldReturnBadRequestWithInvalidRequest() throws Exception {

        mockMvc.perform(
                post("/schedule/generate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new ScheduleGenerationRequest()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void scheduleGenerationShouldReturnOkWithValidRequest() throws Exception {

        Manager manager = Util.createValidManager();

        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(scheduleGenerationService.generateSchedulesForWeek(any(), any()))
                .thenReturn(List.of(Util.createValidScheduleForWeek()));

        mockMvc.perform(
                post("/schedule/generate")
                        .header("Authorization", "")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new ScheduleGenerationRequest()))
        ).andExpect(status().isOk());
    }

    @Test
    void scheduleGenerationShouldReturnUnprocessableEntityWithEmptyScheduleList() throws Exception {

        Manager manager = Util.createValidManager();

        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(scheduleGenerationService.generateSchedulesForWeek(any(), any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(
                post("/schedule/generate")
                        .header("Authorization", "")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new ScheduleGenerationRequest()))
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void saveShouldReturnBadRequestWithInvalidSchedule() throws Exception {

        Manager manager = Util.createValidManager();

        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);

        mockMvc.perform(
                post("/schedule")
                        .header("Authorization", "")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(null))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnOkWithValidSchedule() throws Exception {

        Manager manager = Util.createValidManager();

        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);

        mockMvc.perform(
                post("/schedule")
                        .header("Authorization", "")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Util.createValidScheduleForWeek()))
        ).andExpect(status().isOk());
    }

}
