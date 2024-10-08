package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.repository.ManagerRepository;
import shift.scheduler.backend.util.exception.EntityValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ManagerService extends UserService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public User save(User user) throws EntityValidationException {

        if (user.getAccount() == null)
            throw new EntityValidationException("Missing account details");

        Manager manager = (Manager) user;

        try {
            managerRepository.save(manager);
            return manager;
        } catch (DataIntegrityViolationException e) {
            String field = extractInvalidField(e);
            String message = (field != null) ? String.format("Invalid %s", field) : e.getMessage();

            throw new EntityValidationException(message);
        } catch (Exception e) {
            throw new EntityValidationException(e.getMessage());
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

    @Override
    @Transactional
    public boolean deleteByUsername(String username) {
        try {
            managerRepository.deleteByAccountUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
