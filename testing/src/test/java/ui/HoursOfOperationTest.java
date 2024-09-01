package ui;

import org.junit.jupiter.api.*;
import shift.scheduler.framework.ApiClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class HoursOfOperationTest extends UITest {

    @BeforeEach
    void setUp() {

        super.setUp();
        site.setCurrentUser("manager", "password123", "MANAGER");
        site.getHoursOfOperationPage().load();
    }

    @BeforeAll
    public static void createAccount() {
        ApiClient.registerManager("manager", "Manager", "password123", "Test Company", "City");
    }

    @AfterAll
    public static void deleteAccount() {
        ApiClient.deleteUser("manager");
    }

    @Test
    void shouldBeAbleToSetHoursOfOperation() {

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
}
