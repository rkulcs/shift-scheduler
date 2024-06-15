package shift.scheduler.backend.util.algorithm;

import shift.scheduler.backend.model.*;

import java.util.*;

public class DailyScheduleGenerator extends GeneticAlgorithm<ScheduleForDay, Shift> {

    public int MAX_NUM_GENERATED_SCHEDULES = 1000;

    private HoursOfOperation period;
    private List<TimePeriod> blocks;
    private short numEmployeesPerHour;

    public DailyScheduleGenerator(HoursOfOperation period, short numEmployeesPerHour) {
        this.period = period;
        this.blocks = (List<TimePeriod>) period.getTimeBlocks();
        this.numEmployeesPerHour = numEmployeesPerHour;
    }

    @Override
    public List<ScheduleForDay> generateInitialPopulation(List<List<Shift>> components) {

        List<ScheduleForDay> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++)
            population.add(generateRandomSchedule(components));

        return population;
    }

    @Override
    public long computeFitnessScore(ScheduleForDay schedule) {

        Map<Employee, Integer> numHoursPerEmployee = new HashMap<>();

        for (var shift : schedule.getShifts()) {
            Employee employee = shift.getEmployee();
            int length = shift.getLength();

            if (numHoursPerEmployee.containsKey(employee))
                numHoursPerEmployee.put(employee, numHoursPerEmployee.get(employee)+length);
            else
                numHoursPerEmployee.put(employee, length);
        }

        long score = 0;

        for (var entry : numHoursPerEmployee.entrySet()) {
            Employee employee = entry.getKey();
            int hours = entry.getValue();

            if (hours < employee.getMinHoursPerDay())
                score += (employee.getMaxHoursPerDay() - hours);
            else if (hours > employee.getMaxHoursPerDay())
                score += (hours - employee.getMaxHoursPerDay());
        }

        return score;
    }

    @Override
    public List<ScheduleForDay> crossover(ScheduleForDay a, ScheduleForDay b) {

        List<Shift> shiftsFromA = new ArrayList<>(a.getShifts());
        List<Shift> shiftsFromB = new ArrayList<>(b.getShifts());

        Shift shiftA = shiftsFromA.remove(random.nextInt(shiftsFromA.size()));
        Employee employee = shiftA.getEmployee();

        Shift shiftB = shiftsFromB.stream().filter(shift -> shift.getEmployee() == employee).findFirst().orElse(null);

        if (shiftB == null)
            shiftB = shiftsFromB.get(random.nextInt(shiftsFromB.size()));

        shiftsFromB.remove(shiftB);

        shiftsFromA.add(shiftB);
        shiftsFromB.add(shiftA);

        List<ScheduleForDay> offspring = new ArrayList<>();
        offspring.add(new ScheduleForDay(a.getDay(), shiftsFromA));
        offspring.add(new ScheduleForDay(b.getDay(), shiftsFromB));

        return offspring;
    }

    @Override
    public ScheduleForDay mutate(List<List<Shift>> components, ScheduleForDay schedule) {

        List<Shift> employeeShifts = components.get(random.nextInt(components.size()));
        Shift randomShift = employeeShifts.get(random.nextInt(employeeShifts.size()));

        boolean swappedShifts = false;
        List<Shift> scheduleShifts = (List<Shift>) schedule.getShifts();

        for (int i = 0; i < scheduleShifts.size(); i++) {
            Shift shift = scheduleShifts.get(i);

            if (shift.getEmployee() == randomShift.getEmployee()) {
                scheduleShifts.set(i, randomShift);
                swappedShifts = true;
                break;
            }
        }

        if (!swappedShifts)
            scheduleShifts.set(random.nextInt(scheduleShifts.size()), randomShift);

        return schedule;
    }

    private ScheduleForDay generateRandomSchedule(List<List<Shift>> components) {

        Map<TimePeriod, List<Shift>> map = new HashMap<>();
        blocks.forEach(block -> map.put(block, new ArrayList<>()));

        List<List<Shift>> candidateShifts = new ArrayList<>(components);

        Collection<Shift> shifts = new ArrayList<>();

        while (!candidateShifts.isEmpty() && !blocks.stream().allMatch(block -> map.get(block).size() >= numEmployeesPerHour)) {
            int i = random.nextInt(candidateShifts.size());
            List<Shift> employeeShifts = candidateShifts.remove(i);

            Shift shift = employeeShifts.get(random.nextInt(employeeShifts.size()));

            for (var block : blocks) {
                if (shift.contains(block)) {
                    map.get(block).add(shift);
                    shifts.add(shift);
                }
            }
        }

        return new ScheduleForDay(period.getDay(), shifts);
    }
}
