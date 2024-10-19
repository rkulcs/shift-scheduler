package shift.scheduler.backend.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.util.exception.AuthenticationException;
import shift.scheduler.backend.util.exception.ErrorSource;

import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtService jwtService;

    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public String register(RegistrationRequestDTO request) {

        if (accountService.exists(request.account().username()))
            throw new AuthenticationException(List.of("Username taken"));

        Account account = modelMapper.map(request.account(), Account.class);

        // Hash the password
        account.setPassword(passwordEncoder.encode(request.account().password()));

        if (account.getRole() == Role.MANAGER) {
            if (companyService.exists(request.company().name(), request.company().location()))
                throw new AuthenticationException(List.of("Company already exists"));

            Company company = modelMapper.map(request.company(), Company.class);

            Manager manager = new Manager(account);
            manager.setCompany(company);

            userService.save(manager)
                    .orElseThrow(() -> new AuthenticationException(List.of("Failed to save manager"), ErrorSource.SERVER));
        } else {
            Company company = companyService.findByNameAndLocation(request.company().name(), request.company().location())
                    .orElseThrow(() -> new AuthenticationException(List.of("Company does not exist")));

            Employee employee = new Employee(account, company);

            userService.save(employee)
                    .orElseThrow(() -> new AuthenticationException(List.of("Failed to save employee"), ErrorSource.SERVER));
        }

        String token = jwtService.generateToken(account);
        jwtService.saveToken(token);

        return token;
    }

    public String login(LoginRequestDTO request) {

        Account account = accountService.findByUsername(request.username())
                .orElseThrow(() -> new AuthenticationException(List.of("Invalid username")));

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            throw new AuthenticationException(List.of("Invalid password"));

        return jwtService.findOrCreateToken(account);
    }

    public void logout(@NotNull String authHeaderWithToken) {

        String[] headerComponents = authHeaderWithToken.split(" ");

        if (headerComponents.length != 2)
            throw new AuthenticationException(List.of("Invalid JWT in authorization header"));

        String token = headerComponents[1];

        if (jwtService.deleteToken(token))
            return;

        throw new AuthenticationException(List.of("Failed to log out"), ErrorSource.SERVER);
    }

    public User getUserFromHeader(String authHeader) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String username = jwtService.extractUsername(token);

        return userService.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(List.of("User does not exist")));
    }
}
