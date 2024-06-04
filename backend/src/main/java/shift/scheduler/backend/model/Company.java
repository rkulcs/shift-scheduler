package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import shift.scheduler.backend.model.view.CompanyViews;

import java.util.Collection;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue
    @JsonView(CompanyViews.Public.class)
    private Long id;

    @NotNull
    @NotBlank
    @JsonView(CompanyViews.Public.class)
    private String name;

    @NotNull
    @NotBlank
    @JsonView(CompanyViews.Public.class)
    private String location;

    @OneToMany(mappedBy = "company")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<HoursOfOperation> hoursOfOperation;

    @OneToOne(mappedBy = "company")
    private Manager manager;

    @OneToMany(mappedBy = "company")
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

    public @NotNull @NotBlank String getName() {
        return name;
    }

    public void setName(@NotNull @NotBlank String name) {
        this.name = name;
    }

    public @NotNull @NotBlank String getLocation() {
        return location;
    }

    public void setLocation(@NotNull @NotBlank String location) {
        this.location = location;
    }

    public Collection<HoursOfOperation> getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(Collection<HoursOfOperation> hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;

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
