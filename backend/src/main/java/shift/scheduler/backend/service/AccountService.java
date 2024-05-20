package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.exception.EntityValidationException;

@Service
public class AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void hashPassword(Account account) throws EntityValidationException {

        if (!Account.validatePassword(account.getPassword()))
            throw new EntityValidationException("Invalid password");

        account.setPasswordHash(passwordEncoder.encode(account.getPassword()));
    }

    public boolean validatePassword(Account credentials, User user) {

        String password = credentials.getPassword();
        String passwordHash = user.getPasswordHash();

        return passwordEncoder.matches(password, passwordHash);
    }
}
