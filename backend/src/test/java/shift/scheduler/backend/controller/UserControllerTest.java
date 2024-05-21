package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.config.WebSecurityConfig;
import shift.scheduler.backend.config.filter.JwtAuthenticationFilter;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.LoginRequest;
import shift.scheduler.backend.payload.RegistrationRequest;
import shift.scheduler.backend.service.*;
import shift.scheduler.backend.util.exception.EntityValidationException;
import shift.scheduler.backend.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static shift.scheduler.backend.service.AuthenticationService.AuthenticationResult;

@SpringBootTest
public class UserControllerTest {

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

    private MockMvc mockMvc;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Load MockMVC from web application context to avoid 403 responses
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void registrationShouldFailWithInvalidDetails() throws Exception {

        when(authenticationService.register(any())).thenReturn(new AuthenticationResult(null, "Invalid details"));

        String json = createLoginRequestBody();
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrationShouldSucceedWithValidDetails() throws Exception {

        when(authenticationService.register(any())).thenReturn(new AuthenticationResult(Util.MOCK_JWT, null));

        String json = createLoginRequestBody();
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldFailWithInvalidDetails() throws Exception {

        when(authenticationService.login(any())).thenReturn(new AuthenticationResult(null, "Invalid details"));

        String json = createLoginRequestBody();
        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldSucceedWithValidDetails() throws Exception {

        when(authenticationService.login(any())).thenReturn(new AuthenticationResult(Util.MOCK_JWT, null));

        String json = createLoginRequestBody();
        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk());
    }

    private String createRegistrationRequestBody() throws Exception {

        RegistrationRequest request = new RegistrationRequest(Role.MANAGER, "username", "Name", "password");
        return objectMapper.writeValueAsString(request);
    }

    private String createLoginRequestBody() throws Exception {

        LoginRequest request = new LoginRequest("username", "password");
        return objectMapper.writeValueAsString(request);
    }
}
