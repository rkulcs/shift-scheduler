package shift.scheduler.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.scheduler.backend.dto.EmployeeSettingsDTO;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.User;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.repository.UserRepository;
import shift.scheduler.backend.util.exception.AuthenticationException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    private final JwtService jwtService;

    private final ModelMapper modelMapper;

    public UserService(UserRepository repository, JwtService jwtService, ModelMapper modelMapper) {

        this.repository = repository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    public boolean exists(String username) {
        return repository.existsByAccountUsername(username);
    }

    public <T extends User> Optional<T> save(T user) {

        try {
            return Optional.of(repository.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByAccountUsername(username);
    }

    public User findByAuthHeaderValue(String header) {

        var token = jwtService.extractTokenFromHeader(header);
        var username = jwtService.extractUsername(token);

        return findByUsername(username)
                .orElseThrow(() -> new AuthenticationException(List.of("Invalid username in JWT")));
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        return repository.deleteByAccountUsername(username) > 0;
    }

    public boolean updateEmployeeSettings(Employee employee, EmployeeSettingsDTO settings) {

        List<TimePeriod> availabilities = settings.availabilities()
                .stream()
                .map(dto -> modelMapper.map(dto, TimePeriod.class))
                .toList();

        employee.setAvailabilities(availabilities);
        employee.setHoursPerDayRange(settings.hoursPerDayRange().start(), settings.hoursPerDayRange().end());
        employee.setHoursPerWeekRange(settings.hoursPerWeekRange().start(), settings.hoursPerWeekRange().end());

        return save(employee).isPresent();
    }
}
