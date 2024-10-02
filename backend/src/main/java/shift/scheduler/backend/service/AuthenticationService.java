package shift.scheduler.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.dto.AuthenticationResultDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;

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

    public AuthenticationResultDTO register(RegistrationRequestDTO request) {

        Account account = modelMapper.map(request.account(), Account.class);

        // Hash the password
        account.setPassword(passwordEncoder.encode(request.account().password()));

        if (account.getRole() == Role.MANAGER) {
            Company company = modelMapper.map(request.company(), Company.class);

            if (companyService.findByNameAndLocation(company.getName(), company.getLocation()) != null)
                return new AuthenticationResultDTO(null, List.of("Company already exists"));

            Manager manager = new Manager(account);
            manager.setCompany(company);

            try {
                managerService.save(manager);
            } catch (Exception e) {
                return new AuthenticationResultDTO(null, List.of(e.getMessage()));
            }
        } else {
            Company company = companyService.findByNameAndLocation(request.company().name(), request.company().location());

            if (company == null)
                return new AuthenticationResultDTO(null, List.of("Company does not exist"));

            Employee employee = new Employee(account, company);

            try {
                employeeService.save(employee);
            } catch (Exception e) {
                return new AuthenticationResultDTO(null, List.of(e.getMessage()));
            }
        }

        String token = jwtService.generateToken(account);
        jwtService.saveToken(token);

        return new AuthenticationResultDTO(token, null);
    }

    public AuthenticationResultDTO login(LoginRequestDTO request) {

        Account account = accountService.findByUsername(request.username());

        if (account == null)
            return new AuthenticationResultDTO(null, List.of("Invalid username"));

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            return new AuthenticationResultDTO(null, List.of("Invalid password"));

        String token = jwtService.findOrCreateToken(account);

        return new AuthenticationResultDTO(token, null);
    }

    public AuthenticationResultDTO logout(String authHeaderWithToken) {

        String[] headerComponents = authHeaderWithToken.split(" ");

        if (headerComponents.length != 2)
            return new AuthenticationResultDTO(null, List.of("Invalid JWT token in authorization header"));

        String token = headerComponents[1];

        boolean isTokenDeleted = jwtService.deleteToken(token);
        var errors = isTokenDeleted ? null : List.of("Failed to log out");

        return new AuthenticationResultDTO(token, errors);
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
