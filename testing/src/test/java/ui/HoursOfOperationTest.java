package ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;
import shift.scheduler.framework.UserSession;
import util.Util;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class HoursOfOperationTest extends UITest {

    private static UserSession setterManager = null;
    private static UserSession removerManager = null;

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @BeforeAll
    public static void createAccounts() {
        setterManager = Util.createManager("setterManager", "Setter Company");
        removerManager = Util.createManager("removerManager", "Remover Company");
    }

    @BeforeAll
    public static void setHours() {

        List<List<Integer>> hours = List.of(
                List.of(4, 12),
                List.of(4, 20),
                List.of(8, 16),
                List.of(8, 20),
                List.of(0, 24),
                List.of(0, 24),
                List.of(12, 16)
        );

        var company = ApiClient.getCompany(setterManager.getToken());

        if (company == null)
            throw new RuntimeException("Failed to set up hours of operation for test case.");

        int id = (int) company.get("id");

        var isHourSettingSuccessful = ApiClient.setHoursOfOperation(setterManager.getToken(), id, hours);

        if (!isHourSettingSuccessful)
            throw new RuntimeException("Failed to set up hours of operation for test case.");
    }

    @AfterAll
    public static void deleteAccounts() {
        ApiClient.deleteUser(removerManager.getUsername());
        ApiClient.deleteUser(setterManager.getUsername());
    }

    @Test
    void shouldBeAbleToSetHoursOfOperation() {

        site.getHoursOfOperationPage().loadWithSession(setterManager);

        List<List<Integer>> hours = List.of(
                List.of(4, 12),
                List.of(4, 20),
                List.of(8, 16),
                List.of(8, 20),
                List.of(0, 24),
                List.of(0, 24),
                List.of(12, 16)
        );

        for (int day = 0; day < hours.size(); day++) {
            int startHour = hours.get(day).getFirst();
            int endHour = hours.get(day).getLast();
            site.getHoursOfOperationPage().setHours(day, startHour, endHour);
        }

        site.getHoursOfOperationPage().submitForm();
        assertNull(site.getHoursOfOperationPage().getFormError());

        // Reload the page to verify that the changes were persisted
        site.getHoursOfOperationPage().load();

        for (int day = 0; day < hours.size(); day++)
            assertEquals(hours.get(day), site.getHoursOfOperationPage().getHours(day));
    }

    @Test
    void shouldNotBeAbleToSetInvalidHoursOfOperation() {

        site.getHoursOfOperationPage().loadWithSession(setterManager);

        site.getHoursOfOperationPage().setHours(1, 16, 12);

        site.getHoursOfOperationPage().submitForm();
        assertEquals("Failed to update hours of operation", site.getHoursOfOperationPage().getFormError());
    }

    @Test
    void shouldBeAbleToRemoveHoursOfOperation() throws InterruptedException {

        site.getHoursOfOperationPage().loadWithSession(removerManager);

        // TODO: Add a loading page to the frontend to avoid having to wait like this
        // Wait to ensure that the React components' states were updated before testing
        Thread.sleep(1000);

        for (int day = 0; day < 7; day++)
            site.getHoursOfOperationPage().unsetHours(day);

        site.getHoursOfOperationPage().submitForm();
        assertNull(site.getHoursOfOperationPage().getFormError());

        // Reload the page to verify that the changes were persisted
        site.getHoursOfOperationPage().load();

        for (int day = 0; day < 7; day++)
            assertNull(site.getHoursOfOperationPage().getHours(day));
    }
}
