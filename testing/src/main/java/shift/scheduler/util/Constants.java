package shift.scheduler.util;

public final class Constants {
    public static final String API_URL = "https://10.5.0.2:8443/api";
    public static final String COMPANY_REGISTRATION_ENDPOINT = String.format("%s/user/register", API_URL);
    public static final String EMPLOYEE_REGISTRATION_ENDPOINT = String.format("%s/user/register", API_URL);
    public static final String USER_DELETION_ENDPOINT = String.format("%s/user", API_URL);

    public static final String APP_URL = "http://10.5.0.4:4173";
    public static final String HOME_URL = String.format("%s/", APP_URL);
    public static final String LOGIN_URL = String.format("%s/login", APP_URL);
    public static final String COMPANY_REGISTRATION_URL = String.format("%s/register-company", APP_URL);
    public static final String EMPLOYEE_REGISTRATION_URL = String.format("%s/register-employee", APP_URL);
}
