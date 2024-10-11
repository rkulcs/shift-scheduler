package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import shift.scheduler.backend.dto.ErrorDTO;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.AccountRepository;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.JwtService;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ControllerTest {

    @MockBean
    private JedisConnectionFactory jedisConnectionFactory;

    @MockBean
    AccountRepository accountRepository;

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

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    protected MockHttpServletRequestBuilder postJson(String endpoint, String payload) {
        return post(endpoint).contentType(MediaType.APPLICATION_JSON_VALUE).content(payload);
    }

    protected static String stringify(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected <T> T deserialize(String string, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(string, clazz);
    }

    protected void mockInvalidAuthHeader() {
        when(jwtService.extractUsername(any())).thenReturn(null);
    }

    protected void mockValidAuthHeader(User user) {
        when(authenticationService.getUserFromHeader(any())).thenReturn(user);
        when(jwtService.extractUsername(any())).thenReturn(user.getUsername());
        when(jwtService.isTokenUsable(any(), any())).thenReturn(true);
    }

    protected ErrorDTO executeUnauthenticatedPostWithUserError(
            String endpoint, String json) throws Exception {

        var result = mockMvc.perform(postJson(endpoint, json))
                .andExpect(status().isBadRequest())
                .andReturn();

        return deserialize(result.getResponse().getContentAsString(), ErrorDTO.class);
    }

    protected ErrorDTO executeAuthenticatedPostWithUserError(
            User user, String endpoint, String json) throws Exception {

        mockValidAuthHeader(user);

        var result = mockMvc.perform(postJson(endpoint, json).header("Authorization", ""))
                .andExpect(status().isBadRequest())
                .andReturn();

        return deserialize(result.getResponse().getContentAsString(), ErrorDTO.class);
    }

    protected void executeAuthenticatedPostWithValidPayload(
            User user, String endpoint, String json) throws Exception {

        mockValidAuthHeader(user);

        var result = mockMvc.perform(postJson(endpoint, json).header("Authorization", ""))
                .andExpect(status().isOk())
                .andReturn();
    }

    protected void executeGetWithInvalidJWT(String endpoint) throws Exception {

        mockInvalidAuthHeader();

        mockMvc.perform(get(endpoint).header("Authorization", "Bearer JWT"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    protected void executeAuthenticatedGet(User user, String endpoint) throws Exception {

        mockValidAuthHeader(user);

        mockMvc.perform(get(endpoint).header("Authorization", ""))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void executeRequestWithInvalidJWT(Function<String, MockHttpServletRequestBuilder> method, String endpoint) throws Exception {

        mockInvalidAuthHeader();

        mockMvc.perform(method.apply(endpoint).header("Authorization", ""))
                .andExpect(status().isBadRequest());
    }
}
