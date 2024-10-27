package shift.scheduler.backend.model.period;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Day {
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN;

    private Day() {}

    /**
     * Converts the Day instance to the integer representation of the corresponding day
     * in the DayOfWeek enum class (in which days start from Sunday instead of Monday).
     */
    @JsonIgnore
    public int toDayOfWeekValue() {
        return (ordinal() + 1) % 7;
    }
}
