package shift.scheduler.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.EmployeeService;
import shift.scheduler.backend.util.Util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerTest extends ControllerTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

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
    }
}
