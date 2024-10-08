package shift.scheduler.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.util.Util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static shift.scheduler.backend.service.AuthenticationService.AuthenticationResult;

@SpringBootTest
public class UserControllerTest extends ControllerTest {

    @Test
    void registrationShouldFailWithInvalidDetails() throws Exception {

        when(authenticationService.register(any())).thenReturn(new AuthenticationResult(null, "Invalid details"));

        String json = createRegistrationRequestBody();
        mockMvc.perform(postJson("/user/register", json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrationShouldSucceedWithValidDetails() throws Exception {

        when(authenticationService.register(any())).thenReturn(new AuthenticationResult(Util.MOCK_JWT, null));

        String json = createRegistrationRequestBody();
        mockMvc.perform(postJson("/user/register", json))
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldFailWithInvalidDetails() throws Exception {

        when(authenticationService.login(any())).thenReturn(new AuthenticationResult(null, "Invalid details"));

        String json = createLoginRequestBody();
        mockMvc.perform(postJson("/user/login", json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldSucceedWithValidDetails() throws Exception {

        when(authenticationService.login(any())).thenReturn(new AuthenticationResult(Util.MOCK_JWT, null));

        String json = createLoginRequestBody();
        mockMvc.perform(postJson("/user/login", json))
                .andExpect(status().isOk());
    }

    private String createRegistrationRequestBody() throws Exception {

        RegistrationRequestDTO request = new RegistrationRequestDTO(Role.MANAGER, "username", "Name", "password");
        return stringify(request);
    }

    private String createLoginRequestBody() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO("username", "password");
        return stringify(request);
    }
}
