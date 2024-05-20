package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.AccountService;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.ManagerService;
import shift.scheduler.backend.util.exception.EntityValidationException;
import shift.scheduler.backend.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    ManagerService managerService;

    @MockBean
    AccountService accountService;

    private MockMvc mockMvc;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Load MockMVC from web application context to avoid 403 responses
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void managerRegistrationShouldFailWithInvalidDetails() throws Exception {

        when(managerService.save(any(Manager.class))).thenThrow(new EntityValidationException(""));

        testRegistrationEndpointWithRequestBodies(
                "/user/manager/register",
                createUserJsonStrings(Util.invalidAccounts, Manager::new),
                status().isBadRequest());
    }

    @Test
    void managerRegistrationShouldSucceedWithValidDetails() throws Exception {

        when(managerService.save(any(Manager.class))).thenReturn(null);

        testRegistrationEndpointWithRequestBodies(
                "/user/manager/register",
                createUserJsonStrings(Util.validAccounts, Manager::new),
                status().isOk());
    }

    @Test
    void employeeRegistrationShouldFailWithInvalidDetails() throws Exception {

        when(employeeService.save(any(Employee.class))).thenThrow(new EntityValidationException(""));

        testRegistrationEndpointWithRequestBodies(
                "/user/employee/register",
                createUserJsonStrings(Util.invalidAccounts, Employee::new),
                status().isBadRequest());
    }

    @Test
    void employeeRegistrationShouldSucceedWithValidDetails() throws Exception {

        when(employeeService.save(any(Employee.class))).thenReturn(null);

        testRegistrationEndpointWithRequestBodies(
                "/user/employee/register",
                createUserJsonStrings(Util.validAccounts, Employee::new),
                status().isOk());
    }

    @Test
    void loginShouldFailWithNonexistentUser() throws Exception {

        Account account = Util.validAccounts[0];
        String json = objectMapper.writeValueAsString(account);

        when(employeeService.existsByUsername(account.getUsername())).thenReturn(false);
        when(managerService.existsByUsername(account.getUsername())).thenReturn(false);

        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldFailWithInvalidPassword() throws Exception {

        Account account = Util.validAccounts[0];
        String json = objectMapper.writeValueAsString(account);

        Employee employee = new Employee(account);

        when(employeeService.existsByUsername(account.getUsername())).thenReturn(true);
        when(employeeService.findByUsername(account.getUsername())).thenReturn(employee);
        when(accountService.validatePassword(account, employee)).thenReturn(false);

        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    private void testRegistrationEndpointWithRequestBodies(String endpoint, List<String> requestBodies,
                                                           ResultMatcher matcher) throws Exception {

        for (String json : requestBodies) {
            mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                    .andExpect(matcher);
        }
    }

    private List<String> createUserJsonStrings(Account[] accounts, Supplier<User> constructor) {

        return Arrays.stream(Util.invalidAccounts).map(account -> {
            User user = constructor.get();
            user.setAccount(account);

            try {
                return objectMapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
