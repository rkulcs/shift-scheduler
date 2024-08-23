package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
    boolean existsByAccountUsername(String username);
    Optional<Employee> findByAccountUsername(String username);
    void deleteByAccountUsername(String username);
}
