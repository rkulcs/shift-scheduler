package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.TimePeriod;

import java.io.Serializable;

public class HoursOfOperationId implements Serializable {

    private Company company;
    private TimePeriod.Day day;

    public HoursOfOperationId() {}

    public HoursOfOperationId(Company company, TimePeriod.Day day) {
        this.company = company;
        this.day = day;
    }
}
