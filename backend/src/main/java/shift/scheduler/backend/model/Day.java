package shift.scheduler.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;

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
}
