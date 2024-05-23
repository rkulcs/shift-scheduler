package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

@Service
public class EmployeeService extends UserService  {

    @Autowired
    private EmployeeRepository employeeRepository;

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
}
