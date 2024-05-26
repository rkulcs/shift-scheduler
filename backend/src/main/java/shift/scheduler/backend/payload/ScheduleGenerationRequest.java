package shift.scheduler.backend.payload;

import shift.scheduler.backend.util.validator.Hour;

public class ScheduleGenerationRequest {

    @Hour
    private Short numEmployeesPerHour;

    public ScheduleGenerationRequest() {}

    public Short getNumEmployeesPerHour() {
        return numEmployeesPerHour;
    }

    public void setNumEmployeesPerHour(Short numEmployeesPerHour) {
        this.numEmployeesPerHour = numEmployeesPerHour;
    }
}
