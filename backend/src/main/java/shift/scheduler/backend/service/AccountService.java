package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.AccountRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account findByUsername(String username) {
        return accountRepository.findById(username).orElse(null);
    }
}
