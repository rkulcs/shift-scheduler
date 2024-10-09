package shift.scheduler.backend.util.builder;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.TimePeriod;

import java.util.ArrayList;
import java.util.Collection;

public class CompanyBuilder implements Builder<Company> {

    private String name;

    // Use a default city name so that "setCity" does not need to be called to build a Company instance
    private String city = "City";

    private Collection<TimePeriod> hours = new ArrayList<>();

    public CompanyBuilder() {
    }

    public CompanyBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public CompanyBuilder addHoursOfOperation(Day day, int start, int end) {
        hours.add(new TimePeriod(day, (short) start, (short) end));
        return this;
    }

    @Override
    public Company build() {

        if (name == null)
            throw new IllegalStateException("Name must be set.");

        Company company = new Company("Company", "City", null);
        company.setHoursOfOperation(hours);

        return company;
    }
}
