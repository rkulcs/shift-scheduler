package shift.scheduler.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService<T extends User> {

    private final UserRepository<T> repository;

    public UserService(UserRepository<T> repository) {
        this.repository = repository;
    }

    public boolean exists(String username) {
        return repository.existsByAccountUsername(username);
    }

    public Optional<T> save(T user) {

        try {
            return Optional.of(repository.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<T> findByUsername(String username) {
        return repository.findByAccountUsername(username);
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        return repository.deleteByAccountUsername(username) > 0;
    }
}
