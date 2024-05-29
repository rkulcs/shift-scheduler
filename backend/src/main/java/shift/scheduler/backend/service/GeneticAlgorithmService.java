package shift.scheduler.backend.service;

import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.ScheduleForDay;
import shift.scheduler.backend.model.ScheduleForWeek;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class GeneticAlgorithmService {

    @Autowired
    private KieContainer kieContainer;

    private static final int POPULATION_SIZE = 100;
    private static final int TOURNAMENT_SIZE = 25;

    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.3;

    public Collection<ScheduleForWeek> generateWeeklySchedules(List<Set<ScheduleForDay>> dailySchedules) {

        return null;
    }

    private List<ScheduleForWeek> generateInitialPopulation(List<Set<ScheduleForDay>> dailySchedules) {

        return null;
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
