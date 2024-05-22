package shift.scheduler.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Company;
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
}
