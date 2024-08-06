package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtService jwtService;

    public static class AuthenticationResult {

        String token;
        String error;

        public AuthenticationResult(String token, String error) {
            this.token = token;
            this.error = error;
        }

        public String getToken() {
            return token;
        }

        public String getError() {
            return error;
        }
    }

    public AuthenticationResult register(RegistrationRequestDTO request) {

        if (!Account.validatePassword(request.getPassword()))
            return new AuthenticationResult(null, "Invalid password");

        Account account = new Account(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );

        Company companyDetails = request.getCompany();

        if (companyDetails == null)
            return new AuthenticationResult(null, "Company must be specified");

        if (request.getRole() == Role.EMPLOYEE) {

            Company company = companyService.findByNameAndLocation(companyDetails.getName(), companyDetails.getLocation());

            if (company == null)
                return new AuthenticationResult(null, "Company does not exist");

            Employee employee = new Employee(
                    account,
                    company,
                    request.getMinHoursPerDay(),
                    request.getMaxHoursPerDay(),
                    request.getMinHoursPerWeek(),
                    request.getMaxHoursPerWeek()
            );

            try {
                employeeService.save(employee);
            } catch (EntityValidationException e) {
                return new AuthenticationResult(null, e.getMessage());
            }
        } else if (request.getRole() == Role.MANAGER) {
            Manager manager = new Manager(account);

            if (companyService.findByNameAndLocation(companyDetails.getName(), companyDetails.getLocation()) != null)
                return new AuthenticationResult(null, "Company already exists");

            manager.setCompany(companyDetails);

            try {
                managerService.save(manager);
            } catch (EntityValidationException e) {
                return new AuthenticationResult(null, e.getMessage());
            }
        } else {
            return new AuthenticationResult(null, "Invalid role");
        }

        return new AuthenticationResult(jwtService.generateToken(account), null);
    }

    public AuthenticationResult login(LoginRequestDTO request) {
        Account account = accountService.findByUsername(request.getUsername());

        if (account == null)
            return new AuthenticationResult(null, "Invalid username");

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword()))
            return new AuthenticationResult(null, "Invalid password");

        return new AuthenticationResult(jwtService.generateToken(account), null);
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
