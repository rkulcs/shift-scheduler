package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.dto.AuthenticationResultDTO;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.dto.LoginRequestDTO;
import shift.scheduler.backend.dto.RegistrationRequestDTO;
import shift.scheduler.backend.util.exception.EntityValidationException;

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

    public AuthenticationResultDTO register(RegistrationRequestDTO request) {

        // TODO: Include password requirements in error message
        if (!Account.hasValidPassword(request.password()))
            return new AuthenticationResultDTO(null, "Invalid password");

        Account account = new Account(
                request.username(),
                request.name(),
                passwordEncoder.encode(request.password()),
                request.role()
        );

        Company companyDetails = request.company();

        if (companyDetails == null)
            return new AuthenticationResultDTO(null, "Company must be specified");

        if (request.role() == Role.EMPLOYEE) {

            Company company = companyService.findByNameAndLocation(companyDetails.getName(), companyDetails.getLocation());

            if (company == null)
                return new AuthenticationResultDTO(null, "Company does not exist");

            Employee employee = new Employee(account, company);

            try {
                employeeService.save(employee);
            } catch (EntityValidationException e) {
                return new AuthenticationResultDTO(null, e.getMessage());
            }
        } else if (request.role() == Role.MANAGER) {
            Manager manager = new Manager(account);

            if (companyService.findByNameAndLocation(companyDetails.getName(), companyDetails.getLocation()) != null)
                return new AuthenticationResultDTO(null, "Company already exists");

            manager.setCompany(companyDetails);

            try {
                managerService.save(manager);
            } catch (EntityValidationException e) {
                return new AuthenticationResultDTO(null, e.getMessage());
            }
        } else {
            return new AuthenticationResultDTO(null, "Invalid role");
        }

        String token = jwtService.generateToken(account);
        jwtService.saveToken(token);

        return new AuthenticationResultDTO(token, null);
    }

    public AuthenticationResultDTO login(LoginRequestDTO request) {
        Account account = accountService.findByUsername(request.username());

        if (account == null)
            return new AuthenticationResultDTO(null, "Invalid username");

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            return new AuthenticationResultDTO(null, "Invalid password");

        String token = jwtService.findOrCreateToken(account);

        return new AuthenticationResultDTO(token, null);
    }

    public boolean logout(String token) {
        return jwtService.deleteToken(token);
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
