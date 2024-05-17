package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
}
