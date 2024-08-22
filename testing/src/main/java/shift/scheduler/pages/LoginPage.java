package shift.scheduler.pages;

import org.openqa.selenium.By;
import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.APP_URL;
import static shift.scheduler.util.Constants.LOGIN_URL;

public class LoginPage extends Page {

    public LoginPage(Bot bot) {
        super(bot, LOGIN_URL);
    }

    public boolean logIn(String username, String password) {

        bot.findElement(By.name("username")).sendKeys(username);
        bot.findElement(By.name("password")).sendKeys(password);
        bot.findElement(By.id("login-button")).click();

        // A successful login should result in a redirection to the home page
        return bot.waitUntilOnPage(APP_URL);
    }
}
