package shift.scheduler.backend.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.UserRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

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

    public T save(T user) throws EntityValidationException {

        if (user.getAccount() == null)
            throw new EntityValidationException("Missing account details");

        try {
            repository.save(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            String field = extractInvalidField(e);
            String message = (field != null) ? String.format("Invalid %s", field) : e.getMessage();

            throw new EntityValidationException(message);
        } catch (Exception e) {
            throw new EntityValidationException(e.getMessage());
        }
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

    private String extractInvalidField(Exception e) {

        Pattern pattern = Pattern.compile("Key \\((\\w*)\\)");
        Matcher matcher = pattern.matcher(e.getMessage());

        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }
}
