package shift.scheduler.backend.model.violation;

public abstract class ScheduleConstraintViolation {

    protected int difference;

    public int getDifference() {
        return difference;
    }

    @Override
    public abstract String toString();
}
