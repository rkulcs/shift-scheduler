package shift.scheduler.backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.view.CompanyViews;
import shift.scheduler.backend.service.CompanyService;

import java.util.Collection;

@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(CompanyViews.Public.class)
    public ResponseEntity<Collection<Company>> getAll() {

        Collection<Company> companies = companyService.findAll();

        if (companies == null || companies.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(companies);
    }
}
