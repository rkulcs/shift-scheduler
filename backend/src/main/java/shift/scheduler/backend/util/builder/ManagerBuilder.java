package shift.scheduler.backend.util.builder;

import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.Role;

public class ManagerBuilder implements Builder<Manager> {

    private String username;

    /* Use a default name and password so that it is not necessary to call "setName" and
       "setPassword" when a test account is created */
    private String name = "Manager";
    private String password = "manager123";

    private PasswordEncoder passwordEncoder;
    private Company company;

    public ManagerBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public ManagerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ManagerBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public ManagerBuilder setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public ManagerBuilder setCompany(Company company) {
        this.company = company;
        return this;
    }

    @Override
    public Manager build() {

        if (username == null)
            throw new IllegalStateException("Username must be set.");
        else if (passwordEncoder == null)
            throw new IllegalStateException("Password encoder must be set.");
        else if (company == null)
            throw new IllegalStateException("Company must be set.");

        Account account = new Account(username, name, passwordEncoder.encode(password), Role.MANAGER);
        Manager manager = new Manager(account);
        manager.setCompany(company);

        return manager;
    }
}
