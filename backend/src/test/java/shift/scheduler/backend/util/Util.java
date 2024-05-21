package shift.scheduler.backend.util;

import org.checkerframework.checker.units.qual.A;
import shift.scheduler.backend.model.Account;

public final class Util {

    public static final String MOCK_HASH = "hash";
    public static final String MOCK_JWT = "token";

    public static final Account[] validAccounts = {
            new Account("user", "Test User 1", "password"),
            new Account("user2", "Test User 2", "password"),
    };

    public static final Account[] invalidAccounts = {
            new Account("", "Test User", "password"),
            new Account("username", "", "password"),
            new Account("username", "Test User", null)
    };

}
