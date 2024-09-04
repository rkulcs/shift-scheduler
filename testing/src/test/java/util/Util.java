package util;

import shift.scheduler.framework.ApiClient;
import shift.scheduler.framework.UserSession;

public class Util {

    public static UserSession createEmployee(String username, String companyName) {

        String token = ApiClient.registerEmployee(
                username, "Test User", "password123",
                companyName, "City"
        );

        if (token == null)
            return null;

        return new UserSession(token, username, "EMPLOYEE");
    }

    public static UserSession createManager(String username, String companyName) {

        String token = ApiClient.registerManager(
                username, "Test User", "password123",
                companyName, "City"
        );

        return new UserSession(token, username, "MANAGER");
    }
}
