package shift.scheduler.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean exists(String username) {
        return repository.existsByAccountUsername(username);
    }

    public <T extends User> Optional<T> save(T user) {

        try {
            return Optional.of(repository.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public <T extends User> Optional<T> findByUsername(String username) {
        try {
            return (Optional<T>) repository.findByAccountUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        return repository.deleteByAccountUsername(username) > 0;
    }
}
