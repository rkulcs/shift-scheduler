package shift.scheduler.backend.service;

import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.ScheduleForDay;
import shift.scheduler.backend.model.ScheduleForWeek;
import shift.scheduler.backend.model.Day;

import java.util.*;

@Service
public class GeneticAlgorithmService {

    private class TournamentResult {
        ScheduleForWeek schedule;
        long score;

        TournamentResult(ScheduleForWeek schedule, long score) {
            this.schedule = schedule;
            this.score = score;
        }
    }

    @Autowired
    private KieContainer kieContainer;

    private static final int POPULATION_SIZE = 30000;
    private static final int TOURNAMENT_SIZE = 25;

    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.3;

    private static final int MIN_ITERATIONS = 50;
    private static final int MAX_ITERATIONS = Integer.MAX_VALUE;

    private static final Random random = new Random();

    public Collection<ScheduleForWeek> generateWeeklySchedules(List<List<ScheduleForDay>> dailySchedules) {

        Collection<ScheduleForWeek> generatedSchedules = new ArrayList<>();
        TournamentResult closestSuboptimalSolution = new TournamentResult(null, Long.MAX_VALUE);

        List<ScheduleForWeek> population = generateInitialPopulation(dailySchedules);

        // TODO: Change this depending on the number of daily schedules
        int numIterations = MIN_ITERATIONS;

        for (int i = 0; i < numIterations; i++) {
            List<ScheduleForWeek> nextGeneration = new ArrayList<>();

            while (nextGeneration.size() != POPULATION_SIZE) {
                TournamentResult resultA = performTournament(population);
                ScheduleForWeek a = resultA.schedule;

                if (resultA.score == 0)
                    generatedSchedules.add(a);

                if (resultA.score < closestSuboptimalSolution.score)
                    closestSuboptimalSolution = resultA;

                TournamentResult resultB = performTournament(population);
                ScheduleForWeek b = resultB.schedule;

                if (resultB.score == 0)
                    generatedSchedules.add(b);

                if (resultB.score < closestSuboptimalSolution.score)
                    closestSuboptimalSolution = resultB;

                if (a == b)
                    continue;

                if (random.nextDouble() <= CROSSOVER_RATE) {
                    List<ScheduleForWeek> offspring = crossover(a, b);

                    offspring.forEach(o -> {
                        if (random.nextDouble() <= MUTATION_RATE)
                            o = mutate(dailySchedules, o);

                        nextGeneration.add(o);
                    });
                }
            }

            population = nextGeneration;
        }

        if (generatedSchedules.isEmpty()) {
            generatedSchedules.add(closestSuboptimalSolution.schedule);
        }

        return generatedSchedules;
    }

    /**
     * Creates an initial population of weekly schedules by producing random combinations of daily schedules.
     */
    private List<ScheduleForWeek> generateInitialPopulation(List<List<ScheduleForDay>> dailySchedules) {

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

    private TournamentResult performTournament(List<ScheduleForWeek> schedules) {

        Set<ScheduleForWeek> participants = new HashSet<>();

        while (participants.size() != TOURNAMENT_SIZE) {
            int j = random.nextInt(schedules.size());
            ScheduleForWeek schedule = schedules.get(j);

            if (!participants.contains(schedule))
                participants.add(schedule);
        }

        TournamentResult result = new TournamentResult(null, Long.MAX_VALUE);

        participants.forEach(participant -> {
            long score = computeFitnessScore(participant);

            if (score < result.score) {
                result.schedule = participant;
                result.score = score;
            }
        });

        return result;
    }

    /**
     * Computes a weekly schedule's fitness score based on how many hours each employee works during the given
     * week. The score increases if an employee's number of hours is either below the minimum number of hours
     * that they would like to work per week, or above the maximum number of hours that they can work. If all
     * employees work for a number of hours that is within their specified ranges of hours, then the schedule's
     * fitness score is 0.
     */
    private long computeFitnessScore(ScheduleForWeek schedule) {

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

    private List<ScheduleForWeek> crossover(ScheduleForWeek a, ScheduleForWeek b) {

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

    private ScheduleForWeek mutate(List<List<ScheduleForDay>> dailySchedules, ScheduleForWeek schedule) {

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
