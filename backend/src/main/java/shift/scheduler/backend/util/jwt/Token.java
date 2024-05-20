package shift.scheduler.backend.util;

public class Token {

    public static final String PREFIX = "Bearer ";

    public String value;

    public Token() {}

    public Token(String value) {
        this.value = value;
    }
}
