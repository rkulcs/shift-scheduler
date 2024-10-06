package shift.scheduler.backend.dto;

import shift.scheduler.backend.model.Shift;

import java.time.LocalDate;

public record EmployeeDashboardDataDTO(DetailedShiftDataDTO nextShift, int numShifts, int numHours) {
    public record DetailedShiftDataDTO(Shift shift, LocalDate date) {}
}
