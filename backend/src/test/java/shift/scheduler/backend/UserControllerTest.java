package shift.scheduler.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.controller.UserController;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.repository.ManagerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Load MockMVC from web application context to avoid 403 responses
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void managerRegistrationShouldSucceedWithValidDetails() throws Exception {
        Manager manager = new Manager("test", "Test Manager", "password");
        String json = objectMapper.writeValueAsString(manager);

        mockMvc.perform(post("/user/manager/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void managerRegistrationShouldFailWithInvalidDetails() throws Exception {
        List<Manager> managers = new ArrayList<>();
        managers.add(new Manager("", "Test Manager", "password"));
        managers.add(new Manager("name", "", "password"));
        managers.add(new Manager("name", "Name", ""));

        for (Manager manager : managers) {
            String json = objectMapper.writeValueAsString(manager);

            mockMvc.perform(post("/user/manager/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                    .andExpect(status().isBadRequest());
        }
    }
}
