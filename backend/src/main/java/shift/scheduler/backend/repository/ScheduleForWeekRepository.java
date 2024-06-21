package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.ScheduleForWeek;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleForWeekRepository extends CrudRepository<ScheduleForWeek, Long> {
    Optional<ScheduleForWeek> findByCompanyAndFirstDay(Company company, LocalDate firstDay);
}
