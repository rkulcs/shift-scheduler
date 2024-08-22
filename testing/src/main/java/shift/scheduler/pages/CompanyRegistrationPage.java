package shift.scheduler.pages;

import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.COMPANY_REGISTRATION_URL;

public class CompanyRegistrationPage extends Page {

    public CompanyRegistrationPage(Bot bot) {
        super(bot, COMPANY_REGISTRATION_URL);
    }
}
