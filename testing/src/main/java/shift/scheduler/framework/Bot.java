package shift.scheduler.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

public class Bot {

    private WebDriver driver;
    private WebDriverWait wait;

    public Bot() {
        var options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        driver = new FirefoxDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void getPage(String url) {
        driver.get(url);
    }

    public boolean isOnPage(String url) {

        String currentUrl = driver.getCurrentUrl();
        return currentUrl != null && currentUrl.equals(url);
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public Object execute(String command) {
        return ((FirefoxDriver) driver).executeScript(command);
    }

    public void setSession(UserSession session) {

        setLocalStorage("role", session.getRole());
        setLocalStorage("username", session.getUsername());
        setLocalStorage("token", String.format("Bearer %s", session.getToken()));
    }

    public void setLocalStorage(String key, String value) {
        execute(String.format("localStorage.setItem(\"%s\", \"%s\")", key, value));
    }

    public void waitMillis(long timeoutMillis) {
        wait.withTimeout(Duration.ofMillis(timeoutMillis));
    }

    public boolean waitUntil(Supplier<Boolean> function) {
        return wait.until(d -> function.get());
    }

    public void waitUntilStale(WebElement element) {
        wait.until(ExpectedConditions.stalenessOf(element));
    }

    public boolean waitUntilOnPage(String url) {
        return wait.until(d -> d.getCurrentUrl().equals(url));
    }

    public void quit() {
        driver.quit();
    }
}
