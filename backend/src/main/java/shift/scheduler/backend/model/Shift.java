package shift.scheduler.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Calendar;

//@Entity
public class Shift extends TimePeriod {

    @Temporal(TemporalType.DATE)
    private Calendar date;

    public Shift() {
        super();
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
