package shift.scheduler.backend.service;

import shift.scheduler.backend.model.User;
import shift.scheduler.backend.util.exception.EntityValidationException;

public abstract class UserService {

    public abstract User save(User user) throws EntityValidationException;
    public abstract boolean existsByUsername(String username);
    public abstract User findByUsername(String username);
    public abstract boolean deleteByUsername(String username);
}
