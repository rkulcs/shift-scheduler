package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, String> {
}
