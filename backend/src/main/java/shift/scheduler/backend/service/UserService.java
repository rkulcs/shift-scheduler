package shift.scheduler.backend.service;

import org.springframework.dao.DataIntegrityViolationException;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.exception.EntityValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UserService {

    public abstract User save(User user) throws EntityValidationException;
    public abstract boolean existsByUsername(String username);
    public abstract User findByUsername(String username);
    public abstract boolean deleteByUsername(String username);

    String extractInvalidField(Exception e) {

        Pattern pattern = Pattern.compile("Key \\((\\w*)\\)");
        Matcher matcher = pattern.matcher(e.getMessage());

        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }
}
