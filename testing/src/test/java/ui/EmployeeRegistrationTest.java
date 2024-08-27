package ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EmployeeRegistrationTest extends UITest {

    @BeforeEach
    void setUp() {
        super.setUp();
        site.getEmployeeRegistrationPage().load();
    }

    @BeforeAll
    public static void addUsers() {
        ApiClient.registerManager("manager", "Test Manager", "password123",
                "Company", "City");
        ApiClient.registerEmployee("existingEmployee", "Existing Employee", "password123",
                "Company", "City");
    }

    @AfterAll
    public static void deleteUsers() {
        ApiClient.deleteUser("manager");
    }

    @Test
    void registrationShouldFailWithTakenUsername() {
        site.getEmployeeRegistrationPage().register("existingEmployee", "Name", "password123", 0);
        assertEquals("Invalid username", site.getEmployeeRegistrationPage().getFormError());
    }

    @Test
    void registrationShouldSucceedWithValidDetails() {
        site.getEmployeeRegistrationPage().register("employee", "Name", "password123", 0);
        assertNull(site.getEmployeeRegistrationPage().getFormError());
    }
}
