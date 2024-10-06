package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.view.EntityViews;
import shift.scheduler.backend.util.Period;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class HoursOfOperation extends TimePeriod {

    @ManyToOne
    @JsonIgnore
    private Company company;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(EntityViews.Associate.class)
    private Day day;

    public HoursOfOperation() {}

    public HoursOfOperation(Short startHour, Short endHour, Day day) {

        super(startHour, endHour);
        this.day = day;
    }

    public HoursOfOperation(Short startHour, Short endHour, Company company, Day day) {

        super(startHour, endHour);
        this.company = company;
        this.day = day;
    }

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

    /**
     * Get the time blocks that make up the hours of operation.
     * For example, if the hours of operation are 4-16, and a time block is 4 hours,
     * then this function will return the following periods: 4-8, 8-12, 12-16.
     */
    @JsonIgnore
    public Collection<TimePeriod> getTimeBlocks() {

        Collection<TimePeriod> blocks = new ArrayList<>();

        for (short time = getStart(); time < getEnd(); time += Period.HOURS)
            blocks.add(new TimePeriod(time, (short) (time+Period.HOURS)));

        return blocks;
    }
}
