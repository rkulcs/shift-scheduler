package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.ScheduleForWeek;

public interface ScheduleForWeekRepository extends CrudRepository<ScheduleForWeek, Long> {
}
