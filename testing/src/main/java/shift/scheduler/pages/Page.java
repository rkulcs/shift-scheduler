package shift.scheduler.pages;

import org.openqa.selenium.support.ui.LoadableComponent;
import shift.scheduler.framework.Bot;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class Page extends LoadableComponent<Page> {

    Bot bot;
    String url;

    Page(Bot bot, String url) {
        this.bot = bot;
        this.url = url;
    }

    @Override
    public void load() {
        bot.getPage(url);
    }

    @Override
    public void isLoaded() throws Error {
        assertTrue(bot.isOnPage(url));
    }
}
