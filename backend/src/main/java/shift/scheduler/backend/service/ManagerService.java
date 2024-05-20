package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.ManagerRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

@Service
public class ManagerService extends UserService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public User save(User user) throws EntityValidationException {

        if (user.getAccount() == null)
            throw new EntityValidationException("Missing account details");

        accountService.hashPassword(user.getAccount());

        Manager manager = (Manager) user;

        try {
            managerRepository.save(manager);
            return manager;
        } catch (Exception e) {
            throw new EntityValidationException("Invalid user details");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return managerRepository.existsByAccountUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return managerRepository.findByAccountUsername(username).orElse(null);
    }
}
