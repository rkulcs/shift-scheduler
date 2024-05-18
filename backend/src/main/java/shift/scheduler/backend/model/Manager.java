package shift.scheduler.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Manager extends User {

    public Manager() {
        super();
    }

    public Manager(String username, String name, String password) {
        super(username, name, password);
    }
}
