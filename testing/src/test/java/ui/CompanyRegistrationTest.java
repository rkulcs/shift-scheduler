package ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CompanyRegistrationTest extends UITest {

    @BeforeEach
    void setUp() {
        super.setUp();
        site.getCompanyRegistrationPage().load();
    }

    @Test
    void registrationWithValidDetailsShouldSucceed() {

        site.getCompanyRegistrationPage().register(
                "validManager", "Test Manager", "password123",
                "Brand New Company", "City"
        );
        assertNull(site.getCompanyRegistrationPage().getFormError());
    }

    @Test
    void registrationWithTakenUsernameShouldFail() {

        site.getCompanyRegistrationPage().register(
                "existingManager", "Test Manager", "password123",
                "New Company", "City"
        );
        assertEquals("Invalid username", site.getCompanyRegistrationPage().getFormError());
    }

    @Test
    void registrationWithTakenCompanyDetailsShouldFail() {

        site.getCompanyRegistrationPage().register(
                "newManager", "Test Manager", "password123",
                "Existing Company", "City"
        );
        assertEquals("Company already exists", site.getCompanyRegistrationPage().getFormError());
    }

    @BeforeAll
    public static void addExistingUser() {
        ApiClient.registerManager("existingManager", "Existing Manager", "password123",
                "Existing Company", "City");
    }

    @AfterAll
    public static void deleteUsers() {
        ApiClient.deleteUser("existingManager");
        ApiClient.deleteUser("manager");
    }
}
