package shift.scheduler.backend.util.algorithm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.period.TimePeriod;
import shift.scheduler.backend.model.schedule.Schedule;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.util.Period;

import java.util.*;

public class DailyScheduleGenerator extends GeneticAlgorithm<Shift> {

    public int MAX_NUM_GENERATED_SCHEDULES = 1000;

    private TimePeriod period;
    private List<TimePeriod> blocks;
    private short numEmployeesPerHour;

    public DailyScheduleGenerator(TimePeriod period, short numEmployeesPerHour) {
        this.period = period;
        this.blocks = (List<TimePeriod>) getTimeBlocks(period);
        this.numEmployeesPerHour = numEmployeesPerHour;
    }

    @Override
    public List<Schedule> generateInitialPopulation(List<List<Shift>> components) {

        List<Schedule> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++)
            population.add(generateRandomSchedule(components));

        return population;
    }


    @Override
    public List<Schedule> crossover(Schedule schedA, Schedule schedB) {

        ScheduleForDay a = (ScheduleForDay) schedA;
        ScheduleForDay b = (ScheduleForDay) schedB;

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

        List<Schedule> offspring = new ArrayList<>();
        offspring.add(new ScheduleForDay(a.getDay(), shiftsFromA, blocks, numEmployeesPerHour));
        offspring.add(new ScheduleForDay(b.getDay(), shiftsFromB, blocks, numEmployeesPerHour));

        return offspring;
    }

    @Override
    public Schedule mutate(List<List<Shift>> components, Schedule schedule) {

        ScheduleForDay dailySchedule = (ScheduleForDay) schedule;

        List<Shift> employeeShifts = components.get(random.nextInt(components.size()));
        Shift randomShift = employeeShifts.get(random.nextInt(employeeShifts.size()));

        boolean swappedShifts = false;
        List<Shift> scheduleShifts = (List<Shift>) dailySchedule.getShifts();

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

        return dailySchedule;
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
            boolean shiftToBeAddedToSchedule = false;

            for (var block : blocks) {
                if (shift.contains(block)) {
                    map.get(block).add(shift);
                    shiftToBeAddedToSchedule = true;
                }
            }

            if (shiftToBeAddedToSchedule)
                shifts.add(shift);
        }

        return new ScheduleForDay(period.getDay(), shifts, blocks, numEmployeesPerHour);
    }

    /**
     * Get the time blocks that make up the given hours of operation.
     * For example, if the hours of operation are 4-16, and a time block is 4 hours,
     * then this function will return the following periods: 4-8, 8-12, 12-16.
     */
    @JsonIgnore
    public Collection<TimePeriod> getTimeBlocks(TimePeriod period) {

        Collection<TimePeriod> blocks = new ArrayList<>();

        for (short time = period.getStartHour(); time < period.getEndHour(); time += Period.HOURS)
            blocks.add(new TimePeriod(time, (short) (time+Period.HOURS)));

        return blocks;
    }
}
