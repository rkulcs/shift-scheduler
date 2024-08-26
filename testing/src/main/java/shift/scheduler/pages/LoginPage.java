package shift.scheduler.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import shift.scheduler.framework.Bot;

import java.util.List;

import static shift.scheduler.util.Constants.*;

public class LoginPage extends Page implements Form {

    public LoginPage(Bot bot) {
        super(bot, LOGIN_URL);
    }

    public void logIn(String username, String password) {

        bot.findElement(By.name("username")).sendKeys(username);
        bot.findElement(By.name("password")).sendKeys(password);
        bot.findElement(By.id("login-button")).click();
    }

    @Override
    public String getFormError() {
        List<WebElement> elements = bot.findElements(By.className("Mui-error"));

        if (elements.isEmpty())
            return null;

        String error = elements.stream().filter(e -> "p".equals(e.getTagName())).findFirst().get().getText();

        if (error == null || error.isEmpty())
            return null;

        return error;
    }
}
