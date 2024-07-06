package shift.scheduler.backend.model;

import java.time.LocalDate;

public record EmployeeDashboardData(DetailedShiftData shift, int numShifts, int numHours) {
    public record DetailedShiftData(Shift shift, LocalDate date) {}
}
