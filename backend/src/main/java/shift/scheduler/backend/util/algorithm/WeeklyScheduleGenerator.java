package shift.scheduler.backend.util.algorithm;

import shift.scheduler.backend.model.schedule.Schedule;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.model.Day;

import java.util.*;

public class WeeklyScheduleGenerator extends GeneticAlgorithm<ScheduleForDay> {

    /**
     * Creates an initial population of weekly schedules by producing random combinations of daily schedules.
     */
    public List<Schedule> generateInitialPopulation(List<List<ScheduleForDay>> dailySchedules) {

        List<Schedule> population = new ArrayList<>();

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

        boolean isValid = schedule.validate();

        long score = 0;

        if (isValid)
            return score;

        score = schedule.getConstraintViolations()
                .stream()
                .reduce(0, (subtotal, constraint) -> subtotal + Math.abs(constraint.getDifference()), Integer::sum);

        return score;
    }

    public List<Schedule> crossover(Schedule schedA, Schedule schedB) {

        ScheduleForWeek a = (ScheduleForWeek) schedA;
        ScheduleForWeek b = (ScheduleForWeek) schedB;

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

        List<Schedule> offspring = new ArrayList<>();
        offspring.add(new ScheduleForWeek(schedulesFromA));
        offspring.add(new ScheduleForWeek(schedulesFromB));

        return offspring;
    }

    public Schedule mutate(List<List<ScheduleForDay>> dailySchedules, Schedule schedule) {

        ScheduleForWeek weeklySchedule = (ScheduleForWeek) schedule;

        List<ScheduleForDay> components = (List<ScheduleForDay>) weeklySchedule.getDailySchedules();

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
