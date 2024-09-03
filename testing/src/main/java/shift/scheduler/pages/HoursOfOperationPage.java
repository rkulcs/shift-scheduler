package shift.scheduler.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import shift.scheduler.framework.Bot;
import shift.scheduler.pages.components.DayTile;

import java.util.List;

import static shift.scheduler.util.Constants.HOURS_OF_OPERATION_URL;

public class HoursOfOperationPage extends Page implements Form {

    public HoursOfOperationPage(Bot bot) {
        super(bot, HOURS_OF_OPERATION_URL);
    }

    public void setHours(int day, int startHour, int endHour) {

        var tile = getDayTile(day);
        tile.setChecked(true);
        tile.setStartHour(startHour);
        tile.setEndHour(endHour);
    }

    public List<Integer> getHours(int day) {

        var tile = getDayTile(day);

        return tile.isChecked() ? List.of(tile.getStartHour(), tile.getEndHour())
                                : null;
    }

    public void unsetHours(int day) {

        var tile = getDayTile(day);
        tile.setChecked(false);
    }

    public void submitForm() {
        bot.findElement(By.id("update-button")).click();
    }

    @Override
    public String getFormError() {

        try {
            String error =  bot.findElement(By.className("MuiAlert-standardError")).getText();

            if (error == null || error.isEmpty())
                return null;

            return error;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gets the card element which contains the settings of the specified day.
     *
     * @param day The index of the day whose card should be retrieved, where 0
     *            is Monday, and 6 is Sunday
     *
     * @return A DayTile component with methods to interact with the input
     *         fields of the day tile
     */
    private DayTile getDayTile(int day) {
        return new DayTile(bot, bot.findElement(By.id(String.format("tile-%d", day))));
    }
}
