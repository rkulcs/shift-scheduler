package shift.scheduler.backend.model;

import java.time.LocalDate;

public record CompanyDashboardData(DailyScheduleSummary nextDay, int numEmployees, int totalHours) {
    public record DailyScheduleSummary(LocalDate date, int startHour, int endHour) {}
}
