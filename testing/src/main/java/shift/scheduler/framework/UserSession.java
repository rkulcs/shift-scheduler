package shift.scheduler.framework;

public class UserSession {

    private final String token;
    private final String username;
    private final String role;

    public UserSession(String token, String username, String role) {

        if (!role.equals("MANAGER") && !role.equals("EMPLOYEE"))
            throw new IllegalArgumentException("Role must be MANAGER or EMPLOYEE.");

        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
