package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.period.HoursOfOperation;
import shift.scheduler.backend.model.view.EntityViews;

import java.util.Collection;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue
    @JsonView(EntityViews.Public.class)
    private Long id;

    @NotEmpty
    @JsonView(EntityViews.Public.class)
    private String name;

    @NotEmpty
    @JsonView(EntityViews.Public.class)
    private String location;

    @OneToMany(mappedBy = "company", orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JsonView(EntityViews.Associate.class)
    private Collection<HoursOfOperation> hoursOfOperation;

    @OneToOne(mappedBy = "company")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "company")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<Employee> employees;

    public Company() {}

    public Company(String name, String location, Collection<HoursOfOperation> hoursOfOperation) {
        this.name = name;
        this.location = location;
        this.hoursOfOperation = hoursOfOperation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Collection<HoursOfOperation> getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(Collection<HoursOfOperation> hoursOfOperation) {

        if (this.hoursOfOperation == null) {
            this.hoursOfOperation = hoursOfOperation;
        } else {
            /* Copy the references of the provided hours of operation, rather than the
               reference to the provided collection to ensure that it can be updated in
               the DB using Hibernate (https://stackoverflow.com/a/8835704) */
            this.hoursOfOperation.clear();
            this.hoursOfOperation.addAll(hoursOfOperation);
        }

        for (HoursOfOperation period : hoursOfOperation)
            period.setCompany(this);
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }
}
