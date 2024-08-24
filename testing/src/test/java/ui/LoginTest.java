package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shift.scheduler.framework.ApiClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        ApiClient.registerEmployee("username", "Test Employee", "password123", "Company", "Detroit");

        assertTrue(site.getLoginPage().logIn("username", "password123"));

        ApiClient.deleteEmployee("username");
    }
}
