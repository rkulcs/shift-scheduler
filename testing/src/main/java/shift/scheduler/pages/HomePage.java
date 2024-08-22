package shift.scheduler.pages;

import org.openqa.selenium.By;
import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.APP_URL;

public class HomePage extends Page {

    public HomePage(Bot bot) {
        super(bot, APP_URL);
    }

    public void goToLogin() {

        bot.findElement(By.id("about-tab")).click();
        bot.findElement(By.className("home-button")).click();
    }

    public void goToCompanyRegistration() {
        goToPageFromTab("managers-tab");
    }

    public void goToEmployeeRegistration() {
        goToPageFromTab("employees-tab");
    }

    private void goToPageFromTab(String tabId) {

        bot.findElement(By.id(tabId)).click();
        bot.findElement(By.className("home-button")).click();
    }
}
