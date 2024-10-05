package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleForWeekRepository extends JpaRepository<ScheduleForWeek, Long> {
    Optional<ScheduleForWeek> findByCompanyAndFirstDay(Company company, LocalDate firstDay);
}
