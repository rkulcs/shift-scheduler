package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Manager;

import java.util.Optional;

public interface ManagerRepository extends CrudRepository<Manager, String> {
    boolean existsByAccountUsername(String username);
    Optional<Manager> findByAccountUsername(String username);
    void deleteByAccountUsername(String username);
}
