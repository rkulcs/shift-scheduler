package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import shift.scheduler.backend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByAccountUsername(String username);
    Optional<User> findByAccountUsername(String username);

    @Modifying
    int deleteByAccountUsername(String username);
}
