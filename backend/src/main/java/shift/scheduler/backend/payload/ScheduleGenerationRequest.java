package shift.scheduler.backend.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import shift.scheduler.backend.util.validator.Hour;

import java.time.LocalDate;

public class ScheduleGenerationRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Hour
    private Short numEmployeesPerHour;

    public ScheduleGenerationRequest() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Short getNumEmployeesPerHour() {
        return numEmployeesPerHour;
    }

    public void setNumEmployeesPerHour(Short numEmployeesPerHour) {
        this.numEmployeesPerHour = numEmployeesPerHour;
    }
}
