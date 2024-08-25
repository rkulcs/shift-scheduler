package ui;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
public class LoginTest extends UITest {

    @BeforeEach
    void setUp() {
        super.setUp();
        site.getLoginPage().load();
    }

    @Test
    void loginAsNonexistentUserShouldFail() {
        assertFalse(site.getLoginPage().logIn("username", "password"));
    }

    @Test
    void loginWithValidEmployeeDetailsShouldSucceed() throws Exception {
        assertTrue(site.getLoginPage().logIn("employee", "password123"));
    }

    @Test
    void loginWithValidManagerDetailsShouldSucceed() throws Exception {
        assertTrue(site.getLoginPage().logIn("manager", "password123"));
    }

    @BeforeAll
    public static void registerAccounts() {

        ApiClient.registerEmployee("manager", "Test Manager", "password123", "Company", "City");
        ApiClient.registerEmployee("employee", "Test Employee", "password123", "Company", "City");
    }

    @AfterAll
    public static void deleteAccounts() {
        // The deletion of the manager will result in the deletion of the company and its employees
        ApiClient.deleteUser("manager");
    }
}
