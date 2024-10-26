package shift.scheduler.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.dto.TimePeriodDTO;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.repository.CompanyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final ModelMapper modelMapper;

    public CompanyService(CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    public boolean exists(String name, String location) {
        return companyRepository.findByNameAndLocation(name, location).isPresent();
    }

    public Collection<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> findByNameAndLocation(String name, String location) {

        if (name == null || location == null)
            return Optional.empty();

        return companyRepository.findByNameAndLocation(name, location);
    }

    public Optional<Company> findByManager(Manager manager) {

        if (manager == null || manager.getId() == null)
            return Optional.empty();

        return companyRepository.findByManagerId(manager.getId());
    }

    public boolean updateHoursOfOperation(Company company, Collection<TimePeriodDTO> newHoursOfOperation) {

        if (company == null)
            return false;

        var timePeriods = newHoursOfOperation.stream().map(dto -> modelMapper.map(dto, TimePeriod.class)).toList();

        company.setHoursOfOperation(timePeriods);

        try {
            companyRepository.save(company);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
