package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.EmployeeDashboardData;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

import java.util.Collection;
import java.util.Iterator;

@Service
public class EmployeeService extends UserService  {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public User save(User user) throws EntityValidationException {

        if (user.getAccount() == null)
            throw new EntityValidationException("Missing account details");

        Employee employee = (Employee) user;

        try {
            employeeRepository.save(employee);
            return employee;
        } catch (Exception e) {
            throw new EntityValidationException(e.getMessage());
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return employeeRepository.existsByAccountUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return employeeRepository.findByAccountUsername(username).orElse(null);
    }

    public Employee findByAuthHeader(String authHeader) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String username = jwtService.extractUsername(token);
        return (Employee) this.findByUsername(username);
    }

    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
