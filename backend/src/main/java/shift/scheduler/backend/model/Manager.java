package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Manager extends User {

    @Valid
    @NotNull
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
        setCompany(company);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
        company.setManager(this);
    }
}
