package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.scheduler.backend.model.period.TimePeriod;

public interface AvailabilityRepository extends JpaRepository<TimePeriod, String> {
}
