package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.EntityValidationException;

public abstract class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract User save(User user) throws EntityValidationException;
    public abstract boolean existsByUsername(String username);
    public abstract User findByUsername(String username);

    public void hashPassword(User user) throws EntityValidationException {

        if (!User.validatePassword(user.getPassword()))
            throw new EntityValidationException("Invalid password");

        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
    }

    public boolean validatePassword(User credentials) {

        User user = findByUsername(credentials.getUsername());
        String password = credentials.getPassword();
        String passwordHash = user.getPasswordHash();

        return passwordEncoder.matches(password, passwordHash);
    }
}
