package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Manager extends User {

    public Manager() {
        super();
    }

    public Manager(Account account) {
        super(account);
    }
}
