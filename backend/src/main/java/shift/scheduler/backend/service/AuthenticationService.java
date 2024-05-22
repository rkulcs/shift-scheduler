package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.payload.LoginRequest;
import shift.scheduler.backend.payload.RegistrationRequest;
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

    public AuthenticationResult register(RegistrationRequest request) {

        if (!Account.validatePassword(request.getPassword()))
            return new AuthenticationResult(null, "Invalid password");

        Account account = new Account(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );

        Company company = request.getCompany();

        if (company == null)
            return new AuthenticationResult(null, "Company must be specified");

        if (request.getRole() == Role.EMPLOYEE) {
            if (companyService.findByNameAndLocation(company.getName(), company.getLocation()) == null)
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

            if (companyService.findByNameAndLocation(company.getName(), company.getLocation()) != null)
                return new AuthenticationResult(null, "Company already exists");

            manager.setCompany(company);

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

    public AuthenticationResult login(LoginRequest request) {
        Account account = accountService.findByUsername(request.getUsername());

        if (account == null)
            return new AuthenticationResult(null, "Invalid username");

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword()))
            return new AuthenticationResult(null, "Invalid password");

        return new AuthenticationResult(jwtService.generateToken(account), null);
    }
}
