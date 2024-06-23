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
