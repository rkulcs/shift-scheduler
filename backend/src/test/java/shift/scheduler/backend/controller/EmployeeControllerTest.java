package shift.scheduler.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.service.UserService;
import shift.scheduler.backend.util.Util;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerTest extends ControllerTest {

    @MockBean
    private UserService employeeService;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void getShouldReturnBadRequestWithInvalidUser() throws Exception {
        testEndpointWithInvalidUser(MockMvcRequestBuilders::get, "/employee");
    }

    @Test
    void getShouldReturnOkWithValidEmployee() throws Exception {

        mockValidAuthHeader(Util.createValidEmployee());

        mockMvc.perform(
                get("/employee").header("Authorization", "")
        ).andExpect(status().isOk());
    }

    @Test
    void postShouldReturnBadRequestWithInvalidUser() throws Exception {
        testEndpointWithInvalidUser(MockMvcRequestBuilders::post, "/employee");
    }

    @Test
    void postShouldReturnBadRequestWithValidUserAndInvalidInput() throws Exception {

        Employee employee = Util.createValidEmployee();

        mockValidAuthHeader(employee);
        when(employeeService.save(employee)).thenThrow(new ConstraintViolationException(Set.of()));

        mockMvc.perform(
                post("/employee").header("Authorization", "")
                        .contentType("application/json")
                        .content(stringify(Util.createValidEmployee()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void postShouldReturnOkWithValidUserAndInput() throws Exception {

        Employee employee = Util.createValidEmployee();
        Employee updatedEmployee = Util.createValidEmployee();
        updatedEmployee.setAvailabilities(new ArrayList<>());

        mockValidAuthHeader(employee);
        when(employeeService.save(employee)).thenReturn(Optional.of(employee));

        mockMvc.perform(
                postJson("/employee", stringify(updatedEmployee))
                        .header("Authorization", "")
        ).andExpect(status().isOk());
    }

    @Test
    void getDashboardShouldReturnBadRequestWithInvalidUser() throws Exception {
        testEndpointWithInvalidUser(MockMvcRequestBuilders::get, "/employee/dashboard");
    }

    @Test
    void getDashboardShouldReturnOkWithValidUser() throws Exception {

        Employee employee = Util.createValidEmployee();

        mockValidAuthHeader(employee);
        when(authenticationService.getUserFromHeader(any())).thenReturn(employee);
        when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Util.createValidScheduleForWeek());

        mockMvc.perform(
                get("/employee/dashboard").header("Authorization", "")
        ).andExpect(status().isOk());
    }
}
