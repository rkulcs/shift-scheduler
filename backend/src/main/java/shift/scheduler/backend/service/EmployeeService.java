package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.EmployeeDashboardData;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } catch (DataIntegrityViolationException e) {
            String field = extractInvalidField(e);
            String message = (field != null) ? String.format("Invalid %s", field) : e.getMessage();

            throw new EntityValidationException(message);
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

    @Override
    @Transactional
    public boolean deleteByUsername(String username) {
        try {
            employeeRepository.deleteByAccountUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
