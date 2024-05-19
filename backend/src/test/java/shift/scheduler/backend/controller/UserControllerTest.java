package shift.scheduler.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.util.Util;

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
        Manager manager = new Manager(Util.validAccounts[0]);
        String json = objectMapper.writeValueAsString(manager);

        mockMvc.perform(post("/user/manager/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void managerRegistrationShouldFailWithInvalidDetails() throws Exception {
    }
}
