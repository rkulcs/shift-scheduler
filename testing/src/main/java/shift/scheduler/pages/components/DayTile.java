package shift.scheduler.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import shift.scheduler.framework.Bot;

/**
 * Represents a day tile on the hours of operation or employee availability page.
 */
public class DayTile {

    private Bot bot;

    private WebElement root;
    private WebElement checkbox;
    private WebElement startHourSelectInput;
    private WebElement endHourSelectInput;

    public DayTile(Bot bot, WebElement root) {

        this.bot = bot;

        this.root = root;
        checkbox = root.findElement(By.className("PrivateSwitchBase-input"));

        var selectInputs = root.findElements(By.className("MuiSelect-select"));

        if (selectInputs.size() < 2) {
            throw new IllegalStateException(
                    "The component is missing at least one select input for setting the day's hours of operation."
            );
        }

        startHourSelectInput = selectInputs.getFirst();
        endHourSelectInput = selectInputs.get(1);
    }

    public boolean isChecked() {
        return checkbox.isSelected();
    }

    public void setChecked(boolean toBeChecked) {

        boolean isSelected = checkbox.isSelected();

        if ((!isSelected && toBeChecked) || (isSelected && !toBeChecked))
            checkbox.click();
    }

    public int getStartHour() {
        return Integer.parseInt(startHourSelectInput.getText());
    }

    public void setStartHour(int hour) {
        startHourSelectInput.click();
        setHour(hour);
    }

    public int getEndHour() {
        return Integer.parseInt(endHourSelectInput.getText());
    }

    public void setEndHour(int hour) {
        endHourSelectInput.click();
        setHour(hour);
    }

    private void setHour(int hour) {

        var selectedHourElement = root.findElement(By.xpath(String.format("//li[@data-value=%d]", hour)));
        selectedHourElement.click();

        bot.waitUntilStale(selectedHourElement);
    }
}
