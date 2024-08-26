package shift.scheduler.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.COMPANY_REGISTRATION_URL;

public class CompanyRegistrationPage extends Page implements Form {

    public CompanyRegistrationPage(Bot bot) {
        super(bot, COMPANY_REGISTRATION_URL);
    }

    public void register(String username, String name, String password,
                            String companyName, String companyLocation) {

        bot.findElement(By.name("username")).sendKeys(username);
        bot.findElement(By.name("name")).sendKeys(name);
        bot.findElement(By.name("password")).sendKeys(password);
        bot.findElement(By.name("company.name")).sendKeys(companyName);
        bot.findElement(By.name("company.location")).sendKeys(companyLocation);

        bot.findElement(By.id("register-button")).click();
    }

    @Override
    public String getFormError() {

        try {
            String error =  bot.findElement(By.className("MuiAlert-standard")).getText();

            if (error == null || error.isEmpty())
                return null;

            return error;
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
