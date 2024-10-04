package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.User;

import java.util.Optional;

public interface UserRepository<T extends User> extends CrudRepository<T, String> {
    boolean existsByAccountUsername(String username);
    Optional<T> findByAccountUsername(String username);

    @Modifying
    int deleteByAccountUsername(String username);
}
