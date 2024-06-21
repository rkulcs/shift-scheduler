package shift.scheduler.backend.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public final class DateTimeUtil {

    /**
     * Returns the date of the Monday of the week that the given date belongs to.
     */
    public static LocalDate getFirstDayOfWeek(LocalDate date) {

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(date.getYear(), date.getMonth().getValue()-1, date.getDayOfMonth());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }
}
