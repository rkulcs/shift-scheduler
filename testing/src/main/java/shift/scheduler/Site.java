package shift.scheduler;

import org.openqa.selenium.support.ui.LoadableComponent;
import shift.scheduler.framework.ApiClient;
import shift.scheduler.framework.Bot;
import shift.scheduler.pages.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Site {

    private final Bot bot;
    private final Map<String, LoadableComponent<?>> components = new HashMap<>();

    public Site() {
        bot = new Bot();
    }

    public void leave() {
        bot.quit();
    }

    public HomePage getHomePage() {
        return (HomePage) getPage("home", HomePage::new);
    }

    public LoginPage getLoginPage() {
        return (LoginPage) getPage("login", LoginPage::new);
    }

    public CompanyRegistrationPage getCompanyRegistrationPage() {
        return (CompanyRegistrationPage) getPage("companyRegistration", CompanyRegistrationPage::new);
    }

    public EmployeeRegistrationPage getEmployeeRegistrationPage() {
        return (EmployeeRegistrationPage) getPage("employeeRegistration", EmployeeRegistrationPage::new);
    }

    public HoursOfOperationPage getHoursOfOperationPage() {
        return (HoursOfOperationPage) getPage("hoursOfOperation", HoursOfOperationPage::new);
    }

    public void setCurrentUser(String username, String password, String role) {

        String token = ApiClient.logIn(username, password);

        getHomePage().load();

        bot.setLocalStorage("role", role);
        bot.setLocalStorage("username", username);
        bot.setLocalStorage("token", String.format("Bearer %s", token));
    }

    private Page getPage(String key, Function<Bot, Page> constructor) {

        var page = components.get(key);

        if (page == null) {
            page = constructor.apply(bot);
            components.put(key, page);
        }

        return (Page) page;
    }
}
