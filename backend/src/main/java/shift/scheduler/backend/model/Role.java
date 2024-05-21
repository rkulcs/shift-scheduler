package shift.scheduler.backend.model;

public enum Role {
    MANAGER("MANAGER"),
    EMPLOYEE("EMPLOYEE");

    private final String text;

    Role(final String text) {
        this.text = text;
    }
}
