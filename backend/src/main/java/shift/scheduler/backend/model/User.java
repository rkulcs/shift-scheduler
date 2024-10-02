package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    public User() {}

    public User(Account account) {
        this.setAccount(account);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonIgnore
    public String getUsername() {
        return account.getUsername();
    }

    @JsonIgnore
    public String getName() {
        return account.getName();
    }

    public abstract Company getCompany();
}
