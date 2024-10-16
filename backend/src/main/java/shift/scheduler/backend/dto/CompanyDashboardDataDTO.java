package shift.scheduler.backend.dto;

import java.time.LocalDate;

public record CompanyDashboardDataDTO(DailyScheduleSummary nextDay, int numEmployees, int totalHours) {
    public record DailyScheduleSummary(LocalDate date, int startHour, int endHour) {}
}
