package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.scheduler.backend.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
