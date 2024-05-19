package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.ManagerRepository;
import shift.scheduler.backend.util.EntityValidationException;

@Service
public class ManagerService extends UserService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public User save(User user) throws EntityValidationException {

        super.hashPassword(user);

        Manager manager = (Manager) user;

        if (managerRepository.existsById(manager.getUsername()))
            throw new EntityValidationException("Username taken");

        try {
            managerRepository.save(manager);
            return manager;
        } catch (Exception e) {
            throw new EntityValidationException("Invalid user details");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return managerRepository.existsById(username);
    }

    @Override
    public User findByUsername(String username) {
        return managerRepository.findById(username).orElse(null);
    }
}
