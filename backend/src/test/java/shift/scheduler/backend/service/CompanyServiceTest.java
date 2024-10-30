package shift.scheduler.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import shift.scheduler.backend.model.Company;
import shift.scheduler.backend.model.Manager;
import shift.scheduler.backend.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompanyServiceTest extends ServiceTest {

    @Mock
    CompanyRepository companyRepository;

    CompanyService companyService;

    @BeforeEach
    void beforeEach() {
        companyService = new CompanyService(companyRepository, modelMapper);
    }

    @Nested
    class Exists {

        @Test
        void shouldReturnTrueIfCompanyExists() {
            when(companyRepository.findByNameAndLocation(any(), any())).thenReturn(Optional.of(sampleCompany));
            assertTrue(companyService.exists(sampleCompany.getName(), sampleCompany.getLocation()));
        }

        @Test
        void shouldReturnFalseIfCompanyDoesNotExist() {
            when(companyRepository.findByNameAndLocation(any(), any())).thenReturn(Optional.empty());
            assertFalse(companyService.exists(sampleCompany.getName(), sampleCompany.getLocation()));
        }
    }

    @Nested
    class FindAll {

        @ParameterizedTest
        @MethodSource("createCompanyLists")
        void shouldReturnAllCompanies(List<Company> companies) {
            when(companyRepository.findAll()).thenReturn(companies);
            assertEquals(companies, companyService.findAll());
        }

        static List<List<Company>> createCompanyLists() {
            List<List<Company>> lists = new ArrayList<>();

            lists.add(new ArrayList<>());
            lists.add(List.of(new Company(), new Company("C1", "L1")));
            lists.add(List.of(
                    new Company("C1", "L1"),
                    new Company("C2", "L2"),
                    new Company("C3", "L3")
            ));

            return lists;
        }
    }

    @Nested
    class FindById {

        @Test
        void shouldReturnValidCompanyIfOneExistsWithId() {

            when(companyRepository.findById(any())).thenReturn(Optional.of(sampleCompany));

            var result = companyService.findById(0L);
            assertTrue(result.isPresent());
            assertEquals(sampleCompany, result.get());
        }

        @Test
        void shouldReturnEmptyCompanyIfNoCompanyExistsWithId() {
            when(companyRepository.findById(any())).thenReturn(Optional.empty());
            assertTrue(companyService.findById(0L).isEmpty());
        }
    }

    @Nested
    class FindByNameAndLocation {

        @Test
        void shouldReturnValidCompanyIfOneExistsWithNameAndLocation() {

            when(companyRepository.findByNameAndLocation(any(), any())).thenReturn(Optional.of(sampleCompany));

            var result = companyService.findByNameAndLocation(sampleCompany.getName(), sampleCompany.getLocation());
            assertTrue(result.isPresent());
            assertEquals(sampleCompany, result.get());
        }

        @Test
        void shouldReturnEmptyCompanyIfNoCompanyExistsWithId() {
            when(companyRepository.findByNameAndLocation(any(), any())).thenReturn(Optional.empty());
            assertTrue(companyService.findByNameAndLocation("name", "location").isEmpty());
        }
    }

    @Nested
    class FindByManager {

        @Test
        void shouldReturnCompanyIfManagerHasCompany() {

            var manager = mock(Manager.class);

            when(companyRepository.findByManagerId(any())).thenReturn(Optional.of(sampleCompany));
            when(manager.getId()).thenReturn(0L);

            var result = companyService.findByManager(manager);
            assertTrue(result.isPresent());
            assertEquals(sampleCompany, result.get());
        }

        @Test
        void shouldReturnEmptyCompanyIfManagerHasNoCompany() {

            var manager = mock(Manager.class);

            when(manager.getId()).thenReturn(0L);
            when(companyRepository.findByManagerId(any())).thenReturn(Optional.empty());

            assertTrue(companyService.findByManager(manager).isEmpty());
        }
    }

    @Nested
    class UpdateHoursOfOperation {

        @Test
        void shouldReturnFalseIfCompanyIsNull() {
            assertFalse(companyService.updateHoursOfOperation(null, new ArrayList<>()));
        }

        @Test
        void shouldReturnFalseIfHoursCouldNotBeSaved() {
            when(companyRepository.save(any())).thenThrow(RuntimeException.class);
            assertFalse(companyService.updateHoursOfOperation(sampleCompany, new ArrayList<>()));
        }

        @Test
        void shouldReturnTrueIfHoursWereSaved() {
            when(companyRepository.save(any())).thenReturn(sampleCompany);
            assertTrue(companyService.updateHoursOfOperation(sampleCompany, new ArrayList<>()));
        }
    }
}
