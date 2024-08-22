package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import shift.scheduler.Site;

public abstract class UITest {

    Site site;

    void setUp() {
        site = new Site();
    }

    @AfterEach
    void tearDown() {
        site.leave();
    }
}
