package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.repository.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company findByNameAndLocation(String name, String location) {

        if (name == null || location == null)
            return null;

        return companyRepository.findByNameAndLocation(name, location).orElse(null);
    }

    public Company findByManager(Manager manager) {

        if (manager == null || manager.getId() == null)
            return null;

        return companyRepository.findByManagerId(manager.getId()).orElse(null);
    }

    public Company save(Company company) {

        if (company == null)
            return null;

        return companyRepository.save(company);
    }
}
