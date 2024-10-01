package shift.scheduler.backend.util.builder;

import org.springframework.security.crypto.password.PasswordEncoder;
import shift.scheduler.backend.model.*;

import java.util.function.Supplier;

/**
 * An abstract class which contains common operations for creating user accounts, regardless of
 * whether the account belongs to a manager or an employee.
 *
 * @param <T> The type of User that is built by the builder (either Manager or Employee)
 * @param <B> The name of the builder class that extends this class, allowing the non-abstract
 *            builder methods that are defined to return instances of that specific class
 *            (either ManagerBuilder or UserBuilder)
 */
public abstract class UserBuilder<T extends User, B extends UserBuilder<T, B>> implements Builder<T> {

    private String username;

    /* Use a default name and password so that it is not necessary to call "setName" and
       "setPassword" when a test account is created */
    private String name = "User";
    private String password = "password123";

    private PasswordEncoder passwordEncoder;
    private Company company;

    @SuppressWarnings("unchecked")
    public B setUsername(String username) {
        this.username = username;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setName(String name) {
        this.name = name;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setPassword(String password) {
        this.password = password;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B setCompany(Company company) {
        this.company = company;
        return (B) this;
    }

    @Override
    public T build() {

        if (username == null)
            throw new IllegalStateException("Username must be set.");
        else if (passwordEncoder == null)
            throw new IllegalStateException("Password encoder must be set.");
        else if (company == null)
            throw new IllegalStateException("Company must be set.");

        Account account = new Account(username, name, passwordEncoder.encode(password), getRole());

        return createUser(account, company);
    }

    protected abstract T createUser(Account account, Company company);
    protected abstract Role getRole();
}
