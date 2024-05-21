package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Account;

public interface AccountRepository extends CrudRepository<Account, String> {
}
