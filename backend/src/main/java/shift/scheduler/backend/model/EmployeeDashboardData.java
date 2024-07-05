package shift.scheduler.backend.model;

public class EmployeeDashboardData {

    private Shift nextShift;
    private int numShifts;
    private int numHours;

    public EmployeeDashboardData(Shift nextShift, int numShifts, int numHours) {
        this.nextShift = nextShift;
        this.numShifts = numShifts;
        this.numHours = numHours;
    }

    public Shift getNextShift() {
        return nextShift;
    }

    public int getNumShifts() {
        return numShifts;
    }

    public int getNumHours() {
        return numHours;
    }
}
