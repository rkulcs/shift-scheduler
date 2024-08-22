package shift.scheduler.pages;

import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.EMPLOYEE_REGISTRATION_URL;

public class EmployeeRegistrationPage extends Page {

    public EmployeeRegistrationPage(Bot bot) {
        super(bot, EMPLOYEE_REGISTRATION_URL);
    }
}
