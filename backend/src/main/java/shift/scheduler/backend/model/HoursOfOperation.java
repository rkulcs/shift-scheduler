package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.model.id.HoursOfOperationId;

@Entity
@IdClass(HoursOfOperationId.class)
public class HoursOfOperation extends TimePeriod {

    @Id
    @ManyToOne
    private Company company;

    @Id
    @Enumerated(EnumType.ORDINAL)
    private Day day;

    public HoursOfOperation() {}

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
