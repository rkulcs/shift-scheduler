package shift.scheduler.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import shift.scheduler.framework.Bot;

import java.util.List;

import static shift.scheduler.util.Constants.EMPLOYEE_REGISTRATION_URL;

public class EmployeeRegistrationPage extends Page implements Form {

    public EmployeeRegistrationPage(Bot bot) {
        super(bot, EMPLOYEE_REGISTRATION_URL);
    }

    public void register(String username, String name, String password, int companySelection) {

        bot.findElement(By.name("username")).sendKeys(username);
        bot.findElement(By.name("name")).sendKeys(name);
        bot.findElement(By.name("password")).sendKeys(password);

        var companyInput = bot.findElement(By.className("MuiSelect-select"));
        companyInput.click();

        WebElement list = bot.findElements(By.className("MuiList-root")).getLast();

        List<WebElement> options = list.findElements(By.className("MuiMenuItem-root"));

        if (0 <= companySelection && companySelection < options.size()) {
            options.get(companySelection).click();

            // Wait for the options to disappear so that the register button is not obstructed
            bot.waitUntilStale(list);
        }

        bot.findElement(By.id("register-button")).click();
    }

    @Override
    public String getFormError() {

        try {
            String error =  bot.findElement(By.className("MuiAlert-standardError")).getText();

            if (error == null || error.isEmpty())
                return null;

            return error;
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
