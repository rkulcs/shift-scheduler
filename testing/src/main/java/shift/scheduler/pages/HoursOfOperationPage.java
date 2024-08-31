package shift.scheduler.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import shift.scheduler.framework.Bot;

import static shift.scheduler.util.Constants.HOURS_OF_OPERATION_URL;

public class HoursOfOperationPage extends Page implements Form {

    public HoursOfOperationPage(Bot bot) {
        super(bot, HOURS_OF_OPERATION_URL);
    }

    public void setHours(int day, int startHour, int endHour) {

        var tile = getDayTile(day);
        var checkbox = tile.findElement(By.className("PrivateSwitchBase-input"));

        if (!checkbox.isSelected())
            checkbox.click();

        // Get the two select inputs of the tile: the start hour and end hour
        var selectInputs = tile.findElements(By.className("MuiSelect-select"));

        if (selectInputs.size() < 2) {
            throw new IllegalStateException(
                    "The component is missing at least one select input for setting the day's hours of operation."
            );
        }

        // Set the start hour
        selectInputs.getFirst().click();

        var selectedHourElement = tile.findElement(By.xpath(String.format("//li[@data-value=%d]", startHour)));
        selectedHourElement.click();

        bot.waitUntilStale(selectedHourElement);

        // Set the end hour
        selectInputs.get(1).click();
        selectedHourElement = tile.findElement(By.xpath(String.format("//li[@data-value=%d]", endHour)));
        selectedHourElement.click();

        bot.waitUntilStale(selectedHourElement);
    }

    public void unsetHours(int day) {

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
     * @return A web element with a checkbox and two select inputs, which can be used
     *         to specify whether any employees need to work on that day, and to set
     *         the hours of operation of that day
     */
    private WebElement getDayTile(int day) {
        return bot.findElement(By.id(String.format("tile-%d", day)));
    }
}
