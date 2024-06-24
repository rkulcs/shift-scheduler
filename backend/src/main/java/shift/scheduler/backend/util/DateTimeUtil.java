package shift.scheduler.backend.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public final class DateTimeUtil {

    /**
     * Returns the date of the Monday of the week that the given date belongs to.
     */
    public static LocalDate getFirstDayOfWeek(LocalDate date) {

        if (date.getDayOfWeek() == DayOfWeek.MONDAY)
            return date;

        return date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
    }

    public static LocalDate parseLocalDate(String dateString) {

        if (dateString == null)
            return null;

        String[] components = dateString.split("-");

        if (components.length != 3)
            return null;

        return LocalDate.of(Integer.parseInt(components[0]),
                Integer.parseInt(components[1]),
                Integer.parseInt(components[2]));
    }
}
