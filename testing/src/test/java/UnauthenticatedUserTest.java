import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnauthenticatedUserTest {

    private static final String APP_URL = System.getenv("APP_URL");

    private WebDriver driver;
    private static final int IMPLICIT_TIMEOUT_MS = 500;

    @BeforeEach
    public void setup() {
        driver = new FirefoxDriver();
    }

    @Test
    public void testHomePage() throws Exception {

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(IMPLICIT_TIMEOUT_MS));
        driver.get(getPageUrl(""));

        String title = driver.getTitle();
        assertEquals("Shift Scheduler", title);

        WebElement pageLogInButton = driver.findElement(By.className("home-button"));
        assertEquals("log in", pageLogInButton.getText().toLowerCase());

        WebElement managersTab = driver.findElement(By.className("managers-tab"));
        managersTab.click();
        WebElement companyRegistrationButton = driver.findElement(By.className("home-button"));
        assertEquals("company registration", companyRegistrationButton.getText().toLowerCase());

        WebElement employeesTab = driver.findElement(By.className("employees-tab"));
        employeesTab.click();
        WebElement employeeRegistrationButton = driver.findElement(By.className("home-button"));
        assertEquals("employee registration", employeeRegistrationButton.getText().toLowerCase());

        WebElement navbarLoginButton = driver.findElement(By.className("navbar-login-button"));
        navbarLoginButton.click();
        WebElement form = driver.findElement(By.cssSelector("form"));
        assertNotEquals(null, form);

        WebElement navbarHomeButton = driver.findElement(By.className("navbar-home"));
        navbarHomeButton.click();
        WebElement introductions = driver.findElement(By.className("introduction"));
        assertNotEquals(null, introductions);
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }

    private String getPageUrl(String relativePath) {
        return String.format("%s/%s", APP_URL, relativePath);
    }
}
