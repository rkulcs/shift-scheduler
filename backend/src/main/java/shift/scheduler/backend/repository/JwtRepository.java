package shift.scheduler.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shift.scheduler.backend.model.jwt.CachedJwt;

@Repository
public interface JwtRepository extends CrudRepository<CachedJwt, String> {
}
