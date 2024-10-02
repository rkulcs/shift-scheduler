package shift.scheduler.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService<T extends User> {

    private final UserRepository<T> repository;

    public UserService(UserRepository<T> repository) {
        this.repository = repository;
    }

    public boolean exists(String username) {
        return repository.existsByAccountUsername(username);
    }

    public T save(T user) throws Exception {
        repository.save(user);
        return user;
    }

    public User findByUsername(String username) {
        return repository.findByAccountUsername(username).orElse(null);
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        try {
            repository.deleteByAccountUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
