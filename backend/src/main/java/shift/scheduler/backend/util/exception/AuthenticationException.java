package shift.scheduler.backend.util.exception;

import java.util.List;

/**
 * Thrown if user registration, login, or logout fails at any point due to a
 * user error or a server error. The source of the error determines the HTTP
 * status code of the response to an API request.
 */
public class AuthenticationException extends RuntimeException {

    private final List<String> errors;

    // Set the source of the error(s) to be the user by default
    private ErrorSource source = ErrorSource.USER;

    public AuthenticationException(List<String> errors) {
        super();
        this.errors = errors;
    }

    public AuthenticationException(List<String> errors, ErrorSource source) {
        this(errors);
        this.source = source;
    }

    public List<String> getErrors() {
        return errors;
    }

    public ErrorSource getSource() {
        return source;
    }
}
