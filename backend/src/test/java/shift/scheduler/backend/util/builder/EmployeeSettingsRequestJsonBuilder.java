package shift.scheduler.backend.util.builder;

import shift.scheduler.backend.dto.AvailabilityDTO;
import shift.scheduler.backend.dto.EmployeeSettingsDTO;
import shift.scheduler.backend.dto.TimeIntervalDTO;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSettingsRequestJsonBuilder implements Builder<EmployeeSettingsDTO> {

    private TimeIntervalDTO hoursPerDayRange;
    private TimeIntervalDTO hoursPerWeekRange;
    private List<AvailabilityDTO> availabilities;

    public EmployeeSettingsRequestJsonBuilder setHoursPerDayRange(Short min, Short max) {
        hoursPerDayRange = new TimeIntervalDTO(min, max);
        return this;
    }

    public EmployeeSettingsRequestJsonBuilder setHoursPerWeekRange(Short min, Short max) {
        hoursPerWeekRange = new TimeIntervalDTO(min, max);
        return this;
    }

    public EmployeeSettingsRequestJsonBuilder addAvailability(Day day, Short start, Short end) {

        if (availabilities == null)
            availabilities = new ArrayList<>();

        availabilities.add(new AvailabilityDTO(day, start, end));

        return this;
    }

    @Override
    public EmployeeSettingsDTO build() {
        return new EmployeeSettingsDTO(hoursPerDayRange, hoursPerWeekRange, availabilities);
    }
}
