package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import shift.scheduler.backend.model.Company;

import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByNameAndLocation(String name, String location);
}
