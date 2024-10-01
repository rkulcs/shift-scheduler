package shift.scheduler.backend.dto;

import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Role;

// TODO: Update to have company ID instead of Company instance
public record RegistrationRequestDTO(
        Role role, String username, String name, String password, Company company) {
}
