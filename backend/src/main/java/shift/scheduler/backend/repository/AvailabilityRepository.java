package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Availability;

public interface AvailabilityRepository extends CrudRepository<Availability, String> {
}
