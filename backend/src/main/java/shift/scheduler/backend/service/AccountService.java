package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public boolean exists(String username) {
        return accountRepository.findById(username).isPresent();
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findById(username);
    }
}
