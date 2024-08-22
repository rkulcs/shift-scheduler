package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.pages.Page;

@Execution(ExecutionMode.CONCURRENT)
public class HomeTest extends UITest {

    @BeforeEach
    void setUp() {
        super.setUp();
        site.getHomePage().load();
    }

    @Test
    void shouldBeAbleToNavigateToLoginPage() throws Exception {
        testNavigation(site.getHomePage()::goToLogin, site.getLoginPage());
    }

    @Test
    void shouldBeAbleToNavigateToCompanyRegistration() throws Exception {
        testNavigation(site.getHomePage()::goToCompanyRegistration, site.getCompanyRegistrationPage());
    }

    @Test
    void shouldBeAbleToNavigateToEmployeeRegistration() throws Exception {
        testNavigation(site.getHomePage()::goToEmployeeRegistration, site.getEmployeeRegistrationPage());
    }

    private void testNavigation(Runnable navigationMethod, Page target) throws Exception {

        navigationMethod.run();
        target.isLoaded();
    }
}
