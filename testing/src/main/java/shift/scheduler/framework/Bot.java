package shift.scheduler.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Bot {

    private WebDriver driver;
    private WebDriverWait wait;

    public Bot() {
        driver = new FirefoxDriver();
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

    public boolean waitUntilOnPage(String url) {
        return wait.until(d -> d.getCurrentUrl().equals(url));
    }

    public void quit() {
        driver.quit();
    }
}
