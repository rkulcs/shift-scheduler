package shift.scheduler.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.scheduler.backend.model.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndLocation(String name, String location);
    Optional<Company> findByManagerId(Long id);
}
