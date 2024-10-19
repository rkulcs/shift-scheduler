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
}
