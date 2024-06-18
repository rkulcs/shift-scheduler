package shift.scheduler.backend.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import shift.scheduler.backend.util.validator.Hour;

import java.util.Date;

public class ScheduleGenerationRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @Hour
    private Short numEmployeesPerHour;

    public ScheduleGenerationRequest() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Short getNumEmployeesPerHour() {
        return numEmployeesPerHour;
    }

    public void setNumEmployeesPerHour(Short numEmployeesPerHour) {
        this.numEmployeesPerHour = numEmployeesPerHour;
    }
}
