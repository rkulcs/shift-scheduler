package shift.scheduler.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.service.AccountService;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.service.ManagerService;
import shift.scheduler.backend.util.EntityValidationException;
import shift.scheduler.backend.util.Util;

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
    AccountService mockBean;

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

        for (Account account : Util.invalidAccounts) {
            Manager manager = new Manager(account);
            String json = objectMapper.writeValueAsString(manager);

            mockMvc.perform(post("/user/manager/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void managerRegistrationShouldSucceedWithValidDetails() throws Exception {

        when(managerService.save(any(Manager.class))).thenReturn(null);

        for (Account account : Util.validAccounts) {
            Manager manager = new Manager(account);
            String json = objectMapper.writeValueAsString(manager);

            mockMvc.perform(post("/user/manager/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                    .andExpect(status().isOk());
        }
    }
}
