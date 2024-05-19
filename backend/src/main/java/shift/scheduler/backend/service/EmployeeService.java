package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.EmployeeRepository;
import shift.scheduler.backend.util.EntityValidationException;

@Service
public class EmployeeService extends UserService  {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public User save(User user) throws EntityValidationException {

        super.hashPassword(user);

        Employee employee = (Employee) user;

        if (employeeRepository.existsById(employee.getUsername()))
            throw new EntityValidationException("Username taken");

        try {
            employeeRepository.save(employee);
            return employee;
        } catch (Exception e) {
            throw new EntityValidationException("Invalid user details");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return employeeRepository.existsById(username);
    }

    @Override
    public User findByUsername(String username) {
        return employeeRepository.findById(username).orElse(null);
    }
}
