package shift.scheduler.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

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

    public String getUsername() {
        return account.getUsername();
    }

    public String getPasswordHash() {
        return account.getPasswordHash();
    }
}
