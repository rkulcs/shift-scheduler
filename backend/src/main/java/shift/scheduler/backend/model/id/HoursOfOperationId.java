package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Day;

import java.io.Serializable;

public class HoursOfOperationId implements Serializable {

    private Company company;
    private Day day;

    public HoursOfOperationId() {}

    public HoursOfOperationId(Company company, Day day) {
        this.company = company;
        this.day = day;
    }
}
