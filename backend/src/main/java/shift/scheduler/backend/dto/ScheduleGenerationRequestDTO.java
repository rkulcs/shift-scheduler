package shift.scheduler.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import shift.scheduler.backend.util.validator.Hour;

import java.time.LocalDate;

public class ScheduleGenerationRequestDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @Hour
    private Short numEmployeesPerHour;

    public ScheduleGenerationRequestDTO() {}

    public ScheduleGenerationRequestDTO(LocalDate date, Short numEmployeesPerHour) {
        this.date = date;
        this.numEmployeesPerHour = numEmployeesPerHour;
    }

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
