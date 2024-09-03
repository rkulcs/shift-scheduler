package ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import shift.scheduler.framework.ApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class HoursOfOperationTest extends UITest {

    private static final Map<String, String> userTokens = new HashMap<>();

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @BeforeAll
    public static void createAccounts() {
        userTokens.put("removerManager", ApiClient.registerManager("removerManager", "Remover Tester", "password123", "Remover Test Company", "City"));
        userTokens.put("setterManager", ApiClient.registerManager("setterManager", "Setter Tester", "password123", "Setter Test Company", "City"));
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

        String token = userTokens.get("removerManager");

        var company = ApiClient.getCompany(token);

        if (company == null)
            throw new RuntimeException("Failed to set up hours of operation for test case.");

        int id = (int) company.get("id");

        var isHourSettingSuccessful = ApiClient.setHoursOfOperation(token, id, hours);

        if (!isHourSettingSuccessful)
            throw new RuntimeException("Failed to set up hours of operation for test case.");
    }

    @AfterAll
    public static void deleteAccounts() {
        ApiClient.deleteUser("removerManager");
        ApiClient.deleteUser("setterManager");
    }

    @Test
    void shouldBeAbleToSetHoursOfOperation() {

        site.setCurrentUser(userTokens.get("setterManager"), "setterManager", "MANAGER");
        site.getHoursOfOperationPage().load();

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
    void shouldBeAbleToRemoveHoursOfOperation() throws InterruptedException {

        site.setCurrentUser(userTokens.get("removerManager"), "removerManager", "MANAGER");
        site.getHoursOfOperationPage().load();

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
