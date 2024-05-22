package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Manager extends User {

    @OneToOne
    @JoinColumn(name = "company_id")
    @Cascade(CascadeType.ALL)
    private Company company;

    public Manager() {
        super();
    }

    public Manager(Account account) {
        super(account);
    }

    public Manager(Account account, Company company) {
        super(account);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
