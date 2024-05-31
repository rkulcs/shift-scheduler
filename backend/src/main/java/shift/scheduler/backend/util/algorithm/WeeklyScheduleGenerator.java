package shift.scheduler.backend.util.algorithm;

import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.ScheduleForDay;
import shift.scheduler.backend.model.ScheduleForWeek;
import shift.scheduler.backend.model.Day;

import java.util.*;

public class WeeklyScheduleGenerator extends GeneticAlgorithm<ScheduleForWeek, ScheduleForDay> {

    /**
     * Creates an initial population of weekly schedules by producing random combinations of daily schedules.
     */
    public List<ScheduleForWeek> generateInitialPopulation(List<List<ScheduleForDay>> dailySchedules) {

        List<ScheduleForWeek> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Collection<ScheduleForDay> days = new ArrayList<>();

            for (var list : dailySchedules) {
                int j = random.nextInt(list.size());
                days.add(list.get(j));
            }

            ScheduleForWeek schedule = new ScheduleForWeek();
            schedule.setDailySchedules(days);

            population.add(schedule);
        }

        return population;
    }

    /**
     * Computes a weekly schedule's fitness score based on how many hours each employee works during the given
     * week. The score increases if an employee's number of hours is either below the minimum number of hours
     * that they would like to work per week, or above the maximum number of hours that they can work. If all
     * employees work for a number of hours that is within their specified ranges of hours, then the schedule's
     * fitness score is 0.
     */
    public long computeFitnessScore(ScheduleForWeek schedule) {

        Map<Employee, Integer> employeeHours = new HashMap<>();

        for (var dailySchedule : schedule.getDailySchedules()) {
            for (var shift : dailySchedule.getShifts()) {
                Employee employee = shift.getEmployee();
                int hours = shift.getLength();

                if (employeeHours.containsKey(employee))
                    employeeHours.put(employee, employeeHours.get(employee)+hours);
                else
                    employeeHours.put(employee, hours);
            }
        }

        long score = 0L;

        for (var entry : employeeHours.entrySet()) {
            Employee employee = entry.getKey();
            int hours = entry.getValue();

            int min = employee.getMinHoursPerWeek();
            int max = employee.getMaxHoursPerWeek();

            if (hours < min)
                score += (min - hours);
            else if (hours > max)
                score += (hours - max);
        }

        return score;
    }

    public List<ScheduleForWeek> crossover(ScheduleForWeek a, ScheduleForWeek b) {

        List<ScheduleForDay> schedulesFromA = new ArrayList<>(a.getDailySchedules());
        List<ScheduleForDay> schedulesFromB = new ArrayList<>(b.getDailySchedules());

        int x = random.nextInt(schedulesFromA.size());
        int y = -1;

        Day dayToSwap = schedulesFromA.get(x).getDay();

        for (int i = 0; i < schedulesFromB.size(); i++) {
            if (schedulesFromB.get(i).getDay().equals(dayToSwap)) {
                y = i;
                break;
            }
        }

        if (y == -1)
            return new ArrayList<>();

        ScheduleForDay tmp = schedulesFromA.get(x);
        schedulesFromA.set(x, schedulesFromB.get(y));
        schedulesFromB.set(y, tmp);

        List<ScheduleForWeek> offspring = new ArrayList<>();
        offspring.add(new ScheduleForWeek(schedulesFromA));
        offspring.add(new ScheduleForWeek(schedulesFromB));

        return offspring;
    }

    public ScheduleForWeek mutate(List<List<ScheduleForDay>> dailySchedules, ScheduleForWeek schedule) {

        List<ScheduleForDay> components = (List<ScheduleForDay>) schedule.getDailySchedules();

        int i = random.nextInt(components.size());
        Day dayToReplace = components.get(i).getDay();

        List<ScheduleForDay> candidateReplacements = dailySchedules.stream().filter(l -> l.getFirst().getDay().equals(dayToReplace)).findFirst().orElse(null);

        if (candidateReplacements == null)
            return schedule;

        ScheduleForDay replacement = candidateReplacements.get(random.nextInt(candidateReplacements.size()));
        components.set(i, replacement);

        return schedule;
    }
}
