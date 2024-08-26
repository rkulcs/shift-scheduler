package ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LoginTest extends UITest {

    @BeforeEach
    void setUp() {
        super.setUp();
        site.getLoginPage().load();
    }

    @Test
    void loginAsNonexistentUserShouldFail() {

        site.getLoginPage().logIn("username", "password");
        assertEquals("Invalid username", site.getLoginPage().getFormError());
    }

    @Test
    void loginWithValidEmployeeDetailsShouldSucceed() throws Exception {

        site.getLoginPage().logIn("employee", "password123");
        assertNull(site.getLoginPage().getFormError());
    }

    @Test
    void loginWithValidManagerDetailsShouldSucceed() throws Exception {

        site.getLoginPage().logIn("manager", "password123");
        assertNull(site.getLoginPage().getFormError());
    }

    @BeforeAll
    public static void registerAccounts() {

        ApiClient.registerManager("manager", "Test Manager", "password123", "Company", "City");
        ApiClient.registerEmployee("employee", "Test Employee", "password123", "Company", "City");
    }

    @AfterAll
    public static void deleteAccounts() {
        // The deletion of the manager will result in the deletion of the company and its employees
        ApiClient.deleteUser("manager");
    }
}
