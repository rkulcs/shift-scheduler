package shift.scheduler.backend.util.builder;

import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.*;

public class ManagerBuilder extends UserBuilder<Manager, ManagerBuilder> {

    @Override
    protected Manager createUser(Account account, Company company) {

        Manager manager = new Manager(account);
        manager.setCompany(company);

        return manager;
    }

    @Override
    protected Role getRole() {
        return Role.MANAGER;
    }
}
