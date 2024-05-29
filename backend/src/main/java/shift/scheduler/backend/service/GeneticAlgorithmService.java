package shift.scheduler.backend.service;

import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.ScheduleForDay;
import shift.scheduler.backend.model.ScheduleForWeek;

import java.util.*;

@Service
public class GeneticAlgorithmService {

    @Autowired
    private KieContainer kieContainer;

    private static final int POPULATION_SIZE = 100;
    private static final int TOURNAMENT_SIZE = 25;

    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.3;

    private static final int MIN_ITERATIONS = 50;
    private static final int MAX_ITERATIONS = Integer.MAX_VALUE;

    private static final Random random = new Random();

    public Collection<ScheduleForWeek> generateWeeklySchedules(List<List<ScheduleForDay>> dailySchedules) {

        List<ScheduleForWeek> population = generateInitialPopulation(dailySchedules);

        return null;
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

    private ScheduleForWeek performTournament(List<ScheduleForWeek> schedules) {

        return null;
    }

    private long computeFitnessScore(ScheduleForWeek schedule) {

        return 0L;
    }

    private ScheduleForWeek crossover(ScheduleForWeek a, ScheduleForWeek b) {

        return null;
    }

    private ScheduleForWeek mutate(List<Set<ScheduleForDay>> dailySchedules, ScheduleForWeek schedule) {

        return null;
    }
}
