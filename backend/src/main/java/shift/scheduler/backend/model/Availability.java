package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Availability extends TimePeriod {

    public enum Day {
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN
    }

    @Enumerated(EnumType.ORDINAL)
    private Day day;

    public Availability() {}

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
