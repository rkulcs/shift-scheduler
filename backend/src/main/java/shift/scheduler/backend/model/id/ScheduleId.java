package shift.scheduler.backend.model.id;

import shift.scheduler.backend.model.Company;

import java.io.Serializable;
import java.time.LocalDate;

public class ScheduleId implements Serializable {

    private Company company;
    private LocalDate firstDay;

    public ScheduleId() {}

    public ScheduleId(Company company, LocalDate firstDay) {
        this.company = company;
        this.firstDay = firstDay;
    }
}
