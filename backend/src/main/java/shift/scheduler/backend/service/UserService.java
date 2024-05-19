package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.EntityValidationException;

public abstract class UserService {

    public abstract User save(User user) throws EntityValidationException;
    public abstract boolean existsByUsername(String username);
    public abstract User findByUsername(String username);
}
