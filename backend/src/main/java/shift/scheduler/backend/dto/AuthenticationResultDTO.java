package shift.scheduler.backend.dto;

import java.util.List;

public record AuthenticationResultDTO(String token, List<String> errors) {
}
