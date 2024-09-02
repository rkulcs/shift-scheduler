package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shift.scheduler.backend.config.WebSecurityConfig;
import shift.scheduler.backend.config.filter.JwtAuthenticationFilter;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.JwtService;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ControllerTest {

    @MockBean
    private JedisConnectionFactory jedisConnectionFactory;

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    AuthenticationService authenticationService;

    @InjectMocks
    WebSecurityConfig webSecurityConfig;

    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected MockHttpServletRequestBuilder postJson(String endpoint, String payload) {
        return post(endpoint).contentType(MediaType.APPLICATION_JSON_VALUE).content(payload);
    }

    protected String stringify(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected void mockInvalidAuthHeader() {
        when(authenticationService.getUserFromHeader(any())).thenReturn(null);
    }

    protected void mockValidAuthHeader(User user) {
        when(authenticationService.getUserFromHeader(any())).thenReturn(user);
    }

    protected void testEndpointWithInvalidUser(Function<String, MockHttpServletRequestBuilder> method, String endpoint) throws Exception {
        mockInvalidAuthHeader();

        mockMvc.perform(
                method.apply(endpoint).header("Authorization", "")
        ).andExpect(status().isBadRequest());
    }
}
