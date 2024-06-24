package shift.scheduler.backend.util;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeUtilTest {

    @Test
    public void getsValidFirstDayOfWeekWithMondayGiven() throws Exception {

        LocalDate date = LocalDate.of(2024, 6, 24);
        LocalDate result = DateTimeUtil.getFirstDayOfWeek(date);
        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result).isEqualTo(date);
    }

    @Test
    public void getsValidMondayWithAllOtherDaysOfWeek() throws Exception {

        LocalDate expected = LocalDate.of(2024, 6, 24);

        for (int day = 25; day <= 30; day++) {
            LocalDate date = LocalDate.of(2024, 6, 24);
            LocalDate result = DateTimeUtil.getFirstDayOfWeek(date);
            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    public void getsValidMondayWithDifferentMonths() throws Exception {

        LocalDate expected = LocalDate.of(2024, 7, 29);
        LocalDate date = LocalDate.of(2024, 8, 3);

        LocalDate result = DateTimeUtil.getFirstDayOfWeek(date);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getsValidMondayWithDifferentYears() throws Exception {

        LocalDate expected = LocalDate.of(2024, 12, 30);
        LocalDate date = LocalDate.of(2025, 1, 3);

        LocalDate result = DateTimeUtil.getFirstDayOfWeek(date);
        assertThat(result).isEqualTo(expected);
    }
}
