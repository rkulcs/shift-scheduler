package shift.scheduler.backend.model;

import jakarta.persistence.*;
import shift.scheduler.backend.model.id.ShiftId;

import java.util.Calendar;

@Entity
@IdClass(ShiftId.class)
public class Shift extends TimePeriod {

    @Id
    @ManyToOne
    private Employee employee;

    @Id
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
