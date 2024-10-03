package shift.scheduler.backend.service;

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
    private UserService<Manager> managerService;

    @Autowired
    private UserService<Employee> employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    public String register(RegistrationRequestDTO request) {

        Account account = modelMapper.map(request.account(), Account.class);

        // Hash the password
        account.setPassword(passwordEncoder.encode(request.account().password()));

        if (account.getRole() == Role.MANAGER) {
            Company company = modelMapper.map(request.company(), Company.class);

            if (companyService.findByNameAndLocation(company.getName(), company.getLocation()) != null)
                throw new AuthenticationException(List.of("Company already exists"));

            Manager manager = new Manager(account);
            manager.setCompany(company);

            try {
                managerService.save(manager);
            } catch (Exception e) {
                throw new AuthenticationException(List.of(e.getMessage()));
            }
        } else {
            Company company = companyService.findByNameAndLocation(request.company().name(), request.company().location());

            if (company == null)
                throw new AuthenticationException(List.of("Company does not exist"));

            Employee employee = new Employee(account, company);

            try {
                employeeService.save(employee);
            } catch (Exception e) {
                throw new AuthenticationException(List.of(e.getMessage()));
            }
        }

        String token = jwtService.generateToken(account);
        jwtService.saveToken(token);

        return token;
    }

    public String login(LoginRequestDTO request) {

        Account account = accountService.findByUsername(request.username());

        if (account == null)
            throw new AuthenticationException(List.of("Invalid username"));

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            throw new AuthenticationException(List.of("Invalid password"));

        return jwtService.findOrCreateToken(account);
    }

    public void logout(String authHeaderWithToken) {

        String[] headerComponents = authHeaderWithToken.split(" ");

        if (headerComponents.length != 2)
            throw new AuthenticationException(List.of("Invalid JWT token in authorization header"));

        String token = headerComponents[1];

        if (jwtService.deleteToken(token))
            return;

        throw new AuthenticationException(List.of("Failed to log out"), ErrorSource.SERVER);
    }

    public User getUserFromHeader(String authHeader) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String username = jwtService.extractUsername(token);

        Account account = accountService.findByUsername(username);

        return switch (account.getRole()) {
            case MANAGER -> managerService.findByUsername(username);
            case EMPLOYEE -> employeeService.findByUsername(username);
        };
    }
}
